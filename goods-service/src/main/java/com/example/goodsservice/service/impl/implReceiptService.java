package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.client.OrderClient;
import com.example.goodsservice.client.ProductClient;
import com.example.goodsservice.dto.*;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.dto.response.ProductSummary;
import com.example.goodsservice.dto.response.ReceiptSummary;
import com.example.goodsservice.dto.response.ReportImportExport;
import com.example.goodsservice.entity.*;
import com.example.goodsservice.mapper.BathDetailMapper;
import com.example.goodsservice.mapper.BathMapper;
import com.example.goodsservice.repository.DeliveryDetailRepository;
import com.example.goodsservice.repository.DeliveryNoteRepository;
import com.example.goodsservice.repository.ReceiptDetailRepository;
import com.example.goodsservice.repository.ReceiptRepository;
import com.example.goodsservice.service.IDeliveryDetailService;
import com.example.goodsservice.service.IReceiptDetailService;
import com.example.goodsservice.service.IReceiptService;
import com.example.goodsservice.validation.ImportExportRequestValidator;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
public class implReceiptService implements IReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;
    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;
    @Autowired
    private IDeliveryDetailService deliveryDetailService;
    @Autowired
    private IReceiptDetailService receiptDetailService;
    @Autowired
    InventoryClient inventoryClient;
    @Autowired
    OrderClient orderClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    BathMapper bathMapper;
    @Autowired
    BathDetailMapper bathDetailMapper;


    @Override
    public ReceiptSummary getReceiptSummaryForCurrentMonth(Long warehouseId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX);

        // Sử dụng warehouseId khi gọi phương thức repository
        List<Receipt> receipts = receiptRepository.findAllReceiptsInMonth(startOfMonth, endOfMonth, warehouseId);

        long totalReceipts = receipts.size();
        long totalPurchasePrice = receipts.stream()
                .filter(receipt -> receipt.getStatus() != 0)
                .mapToLong(receipt -> receipt.getPurchasePrice() != null ? receipt.getPurchasePrice() : 0)
                .sum();

        return new ReceiptSummary(totalReceipts, totalPurchasePrice);
    }
    @Override
    public ReceiptSummary getReceiptSummaryForCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX);

        List<Receipt> receipts = receiptRepository.findAllReceiptsInMonth(startOfMonth, endOfMonth);

        long totalReceipts = receipts.size();
        long totalPurchasePrice = receipts.stream()
                .filter(receipt -> receipt.getStatus() != 0)
                .mapToLong(receipt -> receipt.getPurchasePrice() != null ? receipt.getPurchasePrice() : 0)
                .sum();

        return new ReceiptSummary(totalReceipts, totalPurchasePrice);
    }

    @Override
    public Receipt getReceiptById(Long id) {
        Optional<Receipt> receipt = receiptRepository.findById(id);
        return receipt.orElse(null);
    }

    @Override
    public List<Receipt> getAllReceipts(Long warehouseId) {
        return receiptRepository.findAllByStatusNotZeroAndWarehouseId(warehouseId);
    }


    @Override
    public List<Receipt> getAllReceiptsForReturn(Long warehouseId) {
        return receiptRepository.findAllByStatusNotZeroAndNotThree(warehouseId);
    }

    @Override
    public boolean updateReceipt(Long id, Receipt receipt) {
        Optional<Receipt> existingReceipt = receiptRepository.findById(id);
        if (existingReceipt.isPresent()) {
            Receipt updatedReceipt = existingReceipt.get();
            updatedReceipt.setReceiptDate(receipt.getReceiptDate());
            updatedReceipt.setSupplier(receipt.getSupplier());
            updatedReceipt.setStatus(receipt.getStatus());
            updatedReceipt.setPurchasePrice(receipt.getPurchasePrice());
            receiptRepository.save(updatedReceipt);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteReceiptUpdateStatus(Long id) {
        if (receiptRepository.existsById(id)) {
            updateReceiptStatus(id,0);
            List<ReceiptDetail> receiptDetails = receiptDetailService.getReceiptDetailsWithReceiptId(id);
            List<Long> listId = new ArrayList<>();
            for ( ReceiptDetail detail : receiptDetails){
                // update So luong kho.
                detail.setQuantity(0);
                detail.setPurchasePrice(0L);
                listId.add(detail.getBatchDetail_Id());
                Boolean tmp = receiptDetailService.updateReceiptDetail(detail.getId(),detail);
                if( tmp == false) System.out.println("Thanh cong chi tiet nhap");
            }


            try {
                inventoryClient.deleteBatchDetailReturnBatchID(listId);
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean updateReceiptStatus(Long id, Integer status) {
        Optional<Receipt> existingReceipt = receiptRepository.findById(id);
        if (existingReceipt.isPresent()) {
            Receipt receipt = existingReceipt.get();
            receipt.setStatus(status);
            receiptRepository.save(receipt);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<DeliveryNote> getDeliveryNotesByReceiptId(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId).orElse(null);
        if (receipt == null) {
            // Xử lý trường hợp không tìm thấy Receipt với id tương ứng
            return Collections.emptyList(); // hoặc có thể ném một ngoại lệ phù hợp
        }
        return receipt.getDeliveryNotes();
    }
    public List<ProductQuantity> getProductQuantities(List<Import_Export_DetailRequest> details) {
        List<ProductQuantity> productQuantities = new ArrayList<>();

        for (Import_Export_DetailRequest detail : details) {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProductId(detail.getProduct_Id());
            productQuantity.setQuantity(detail.getQuantity().longValue());
            productQuantities.add(productQuantity);
        }

        return productQuantities;
    }
    public boolean checkInventory(Long locationId, Long warehouseId,Long quantityProduct){
        Long currentLoad = inventoryClient.getCurrentLoadForLocation(locationId,warehouseId);
        System.out.println("SLSS: "+quantityProduct + ":"+currentLoad);
        if (quantityProduct <= currentLoad) {
            return true;
        }
        return false;
    }
    public void processProductQuantities(Import_Export_Request importExportRequest) {
        List<ProductQuantity> productQuantities = getProductQuantities(importExportRequest.getImport_Export_Details());

        // Tính tổng số lượng
        Long totalQuantity = productQuantities.stream()
                .mapToLong(ProductQuantity::getQuantity)
                .sum();

        // Kiểm tra khả dụng
        boolean isAvailable = checkInventory(
                importExportRequest.getLocation(),
                importExportRequest.getWarehouseId(),
                totalQuantity
        );


        if (!isAvailable) {
            String errorMessage = "Tổng Số lượng mặt hàng vượt quá sức chứa của vị trí bạn chọn.";
            throw new RuntimeException(errorMessage);
        }
    }
    public void processProductQuantitiesTransfer(Import_Export_Request importExportRequest) {
        List<ProductQuantity> productQuantities = getProductQuantities(importExportRequest.getImport_Export_Details());

        // Tính tổng số lượng
        Long totalQuantity = productQuantities.stream()
                .mapToLong(ProductQuantity::getQuantity)
                .sum();

        // Kiểm tra khả dụng
        boolean isAvailable = checkInventory(
                importExportRequest.getLocation(),
                importExportRequest.getWarehouseDestination(),
                totalQuantity
        );


        if (!isAvailable) {
            String errorMessage = "Tổng Số lượng mặt hàng vượt quá sức chứa của vị trí bạn chọn.";
            throw new RuntimeException(errorMessage);
        }
    }

    @Transactional
    public Receipt createReceiptWithDetails(Import_Export_Request importExportRequest) {
        // Khởi tạo Receipt
        processProductQuantities(importExportRequest);
        Receipt savedReceipt = null;
        Long bathId = -1L;
        try {
            // Tạo Batch thông qua API
            BathRequest bathRequest = bathMapper.toBathRequest(importExportRequest);
            bathRequest.setStatus(1);
            Date date = importExportRequest.getExpiryDate();
            if (date == null) date = new Date();
            bathRequest.setBatchName(importExportRequest.getBatchName() + "-" + createBatchName(importExportRequest.getWarehouseId(), date));
            bathRequest.setWarehouseId(importExportRequest.getWarehouseId());
            //
            BathRequest bath = inventoryClient.createBath(bathRequest);
            bathId = bath.getId();

            // Tạo Receipt và lưu vào cơ sở dữ liệu
            Receipt receipt = new Receipt();
            receipt.setReceiptDate(LocalDateTime.now());
            receipt.setSupplier(new Supplier(importExportRequest.getSupplier()));
            receipt.setStatus(1);
            receipt.setPurchasePrice(importExportRequest.getPrice());
            receipt.setWarehouse(new Warehouse(importExportRequest.getWarehouseId()));
            receipt.setEmployeeId(importExportRequest.getEmployeeId());
            //
            try{
                savedReceipt = receiptRepository.save(receipt);
            }catch (Exception e){
                if( bathId != -1L){
                    inventoryClient.deleteBatchById(bathId);
                }
            }
            if( savedReceipt == null){
                throw new RuntimeException("Lỗi tạo phiếu nhập hàng");

            }

            // Tạo ReceiptDetail cho từng sản phẩm trong danh sách
            for (Import_Export_DetailRequest detailRequest : importExportRequest.getImport_Export_Details()) {
                createReceiptDetail(detailRequest, importExportRequest, bath, savedReceipt);
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException("Đã xảy ra lỗi trong quá trình tạo phiếu nhập: " + e.getMessage());
        }

        return savedReceipt;
    }

    // Hàm phụ trợ để tạo ReceiptDetail
    private void createReceiptDetail(Import_Export_DetailRequest detailRequest, Import_Export_Request importExportRequest, BathRequest bath, Receipt savedReceipt) {

        Long bathDetailId =-1L;
        try {
            // Tạo BatchDetail thông qua API
            Batch batchTmp = new Batch();
            batchTmp.setId(bath.getId());
            batchTmp.setWarehouseId(importExportRequest.getWarehouseId());

            Location location = new Location();
            location.setId(importExportRequest.getLocation());
            location.setWarehouseId(importExportRequest.getWarehouseId());

            BathDetailRequest bathDetailRequest = bathDetailMapper.toBathDetailRequest(detailRequest);
            bathDetailRequest.setBatch(batchTmp);
            bathDetailRequest.setLocation(location);

            BathDetailRequest bathDetail = inventoryClient.createDetailBath(bathDetailRequest);


            // Lưu ReceiptDetail vào cơ sở dữ liệu
            ReceiptDetail detail = new ReceiptDetail();
            detail.setReceipt(savedReceipt);
            detail.setPurchasePrice(detailRequest.getPurchasePrice());
            detail.setQuantity(detailRequest.getQuantity());
            detail.setBatchDetail_Id(bathDetail.getId());
            detail.setProductId(detailRequest.getProduct_Id());
            try{
                receiptDetailRepository.save(detail);
            }catch (Exception e){
                if( bathDetailId != -1L){
                    inventoryClient.deleteBatchDetailById(bathDetailId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Đã xảy ra lỗi trong quá trình tạo ReceiptDetail: " + e.getMessage());
        }
    }
    // Hàm tạo số ngẫu nhiên trong khoảng từ 100 đến 999
    public static int generateRandomNumber() {
        Random random = new Random();
        return 100 + random.nextInt(900);
    }

    // Hàm tạo tên batch với định dạng dd/MM/yyyy và số ngẫu nhiên
    public static String createBatchName(Long warehouseId, Date expiryDate) {
        // Định dạng ngày theo dd/MM/yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(expiryDate);

        // Gọi hàm tạo số ngẫu nhiên
        int randomNumber = generateRandomNumber();

        // Kết hợp ID kho, ngày định dạng, và số ngẫu nhiên
        return "CK-" + warehouseId + "-" + formattedDate + "-" + randomNumber;
    }

    @Transactional
    public Receipt createImportTransfer(Import_Export_Request importExportRequest) {
        // check số lượng kho trống
        processProductQuantitiesTransfer(importExportRequest);
        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng
        BathRequest bathRequest = bathMapper.toBathRequest(importExportRequest);
        bathRequest.setStatus(1);
        bathRequest.setWarehouseId(importExportRequest.getWarehouseDestination());
        bathRequest.setBatchName(createBatchName(importExportRequest.getWarehouseId(), importExportRequest.getExpiryDate()));
        BathRequest bath = new BathRequest();
        bath = inventoryClient.createBath(bathRequest);
        System.out.println("ID bath:"+bath.getBatchName());
        Receipt savedReceipt = null;
        Receipt receipt = new Receipt();
        try {
            receipt.setReceiptDate(LocalDateTime.now());
            receipt.setStatus(1);
            receipt.setPurchasePrice(0l);
            receipt.setWarehouse(new Warehouse(importExportRequest.getWarehouseDestination()));
            receipt.setWarehouseTransfer(new Warehouse(importExportRequest.getWarehouseId()));

            // API lấy thông tin đăng nhập để set ID Nhân viên
            receipt.setEmployeeId(importExportRequest.getEmployeeId());
            try{
                savedReceipt = receiptRepository.save(receipt);
            }catch (Exception e){
                inventoryClient.getBathByDetail(bath.getId());
            }
            if (savedReceipt == null)
            {
                throw new RuntimeException("Đã xảy ra lỗi trong tạo nhập chuyển kho: ");

            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

        for (Import_Export_DetailRequest detailRequest : importExportRequest.getImport_Export_Details()) {

            System.out.println("ID Name abc:"+detailRequest.getProduct_Id());
            Batch batchTmp = new Batch();
            batchTmp.setId(bath.getId());
            batchTmp.setWarehouseId(importExportRequest.getWarehouseId());
            Location location = new Location();
            location.setId(importExportRequest.getLocation());
            BathDetailRequest bathDetailRequest = bathDetailMapper.toBathDetailRequest(detailRequest);
            bathDetailRequest.setBatch(batchTmp);
            bathDetailRequest.setLocation(location);
            BathDetailRequest bathDetail = inventoryClient.createDetailBath(bathDetailRequest);

            ReceiptDetail detail = new ReceiptDetail();
            detail.setReceipt(savedReceipt);

            detail.setPurchasePrice(0L);
            detail.setQuantity(detailRequest.getQuantity());
            detail.setBatchDetail_Id(bathDetail.getId());
            detail.setProductId(detailRequest.getProduct_Id());
            System.out.println("99999:"+bath.getBatchName());

            try{
                receiptDetailRepository.save(detail);
            }catch (Exception e){
                inventoryClient.deleteBatchDetailById(detail.getId());
                e.printStackTrace();
            }
        }

        Optional<DeliveryNote> abc = deliveryNoteRepository.findById(importExportRequest.getDeliveryNote());
        abc.get().setStatus(2);
        deliveryNoteRepository.save(abc.get());


        return savedReceipt;
    }

    @Override
    public List<ReportImportExport> createReportImportExport(Integer month, Integer year) {
        return null;
    }

    public static List<ProductQuantity> getLargestList(List<List<ProductQuantity>> lists) {
        return lists.stream()
                .max(Comparator.comparingInt(List::size))
                .orElse(new ArrayList<>());
    }
    // từng kho
    @Override
    public List<ReportImportExport> createReportImportExport(Integer month, Integer year, Long wareHouseId) {
        List<ReportImportExport> reportImportExports = new ArrayList<>();

        // Khởi tạo các danh sách với giá trị mặc định nếu các dịch vụ trả về null
        List<ProductQuantity> list_export_return_order = Optional.ofNullable(orderClient.getProductQuantity_import_order(month, year, wareHouseId))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_import_check_inventory = Optional.ofNullable(inventoryClient.getProductQuantity_import_check_inventory(month, year, wareHouseId))
                .orElse(new ArrayList<>());

        //1
        List<ProductQuantity> list_import_receipt = Optional.ofNullable(receiptDetailService.getProductQuantitiesForMonthYearImportWarehouse(month, year, wareHouseId,1))
                .orElse(new ArrayList<>());
        //2
        List<ProductQuantity> list_export_cancel = Optional.ofNullable(deliveryDetailService.getProductQuantitiesForMonthYearAndType(month, year, 2,wareHouseId))
                .orElse(new ArrayList<>());
        // transfer
        List<ProductQuantity> list_export_transfer = Optional.ofNullable(deliveryDetailService.getProductQuantitiesForMonthYearAndType(month, year, 3,wareHouseId))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_import_transfer = Optional.ofNullable(receiptDetailService.getProductQuantitiesForMonthYearImportWarehouse(month, year, wareHouseId,2))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_export_return_receipt = Optional.ofNullable(deliveryDetailService.getProductQuantitiesForMonthYearAndType(month, year, 1, wareHouseId))
                .orElse(new ArrayList<>());
        //3
        List<ProductQuantity> list_export_check_inventory = Optional.ofNullable(inventoryClient.getProductQuantity_export_check_inventory(month, year,wareHouseId))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_import_order = Optional.ofNullable(orderClient.getProductQuantity_export_return_order(month, year,wareHouseId))
                .orElse(new ArrayList<>());

        List<List<ProductQuantity>> allLists = Arrays.asList(
                list_import_order,
                list_import_check_inventory,
                list_import_receipt,
                list_export_return_receipt,
                list_export_cancel,
                list_export_check_inventory,
                list_export_return_order,
                list_export_transfer,
                list_import_transfer
        );

        List<ProductQuantity> largestList = getLargestList(allLists);

        for (ProductQuantity product : largestList) {
            ReportImportExport reportImportExport = new ReportImportExport();
            Long productId = product.getProductId();

            if (productId != null) {
                Long quantityReturnReceipt = list_export_return_receipt.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);

                Long quantityCancel = list_export_cancel.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);

                Long quantityCheck0 = list_export_check_inventory.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);

                Long quantityReturnOrder = list_export_return_order.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);

                Long quantityOrder = list_import_order.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);

                Long quantityCheck1 = list_import_check_inventory.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);

                Long quantityReceipt = list_import_receipt.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);
                // transfer
                Long quantityTransfer = list_export_transfer.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);
                Long quantityTransferImport = list_import_transfer.stream()
                        .filter(pq -> productId.equals(pq.getProductId()))
                        .map(pq -> Optional.ofNullable(pq.getQuantity()).orElse(0L))
                        .findFirst()
                        .orElse(0L);

                reportImportExport.setImport_transfer(Math.toIntExact(quantityTransferImport));
                reportImportExport.setTransfer(Math.toIntExact(quantityTransfer));

                reportImportExport.setExport_order(Math.toIntExact(quantityOrder));
                reportImportExport.setExport_check(Math.toIntExact(quantityCheck0));
                reportImportExport.setExport_cancel(Math.toIntExact(quantityCancel));
                reportImportExport.setExport_supplier(Math.toIntExact(quantityReturnReceipt));

                reportImportExport.setImport_supplier(Math.toIntExact(quantityReceipt));
                reportImportExport.setImport_check_inventory(Math.toIntExact(quantityCheck1));
                reportImportExport.setImport_return_order(Math.toIntExact(quantityReturnOrder));
                reportImportExport.setId(productId);




                        long a = reportImportExport.getImport_check_inventory()
                        + reportImportExport.getImport_return_order()
                        + reportImportExport.getImport_supplier()
                        + reportImportExport.getImport_transfer();

                        long b = reportImportExport.getExport_cancel()
                        + reportImportExport.getExport_supplier()
                        + reportImportExport.getExport_order()
                                + reportImportExport.getTransfer()
                        + (reportImportExport.getExport_check()*-1);


                long inventory = a-b;
                System.out.println("a:"+a);
                System.out.println("b:"+b);
                System.out.println("inventory:"+inventory);
                        System.out.println("Gia trị tra: "+reportImportExport.getImport_check_inventory()*-1);
                reportImportExport.setInventory((int) inventory);
                try {
                    String nameProduct = productClient.getNameProductByID(productId);
                    reportImportExport.setNameProduct(nameProduct);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Lỗi khi lấy tên sản phẩm");
                }

                reportImportExports.add(reportImportExport);
            }
        }

        return reportImportExports;
    }
    //  fixed not check

    @Override
    public List<ProductSummary> getProductSummaryBySupplierId(Long supplierId, Long warehouseId, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        // Chuyển đổi sang LocalDateTime để phù hợp với tham số trong truy vấn
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(LocalTime.MAX);

        // Lấy dữ liệu từ ReceiptDetail dựa trên supplierId, warehouseId và thời gian tháng hiện tại
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findByReceipt_Supplier_IdAndReceipt_Warehouse_IdAndReceipt_ReceiptDateBetween(
                supplierId,
                warehouseId,
                startDateTime,
                endDateTime);

        // Lấy dữ liệu từ DeliveryDetail dựa trên supplierId, warehouseId và thời gian tháng hiện tại
        List<DeliveryDetail> deliveryDetails = deliveryDetailRepository.findByDeliveryNote_Receipt_Supplier_IdAndDeliveryNote_Receipt_Warehouse_IdAndDeliveryNote_DeliveryDateBetween(
                supplierId,
                warehouseId,
                startDateTime,
                endDateTime);

        Map<Long, ProductSummary> productSummaryMap = new HashMap<>();

        for (ReceiptDetail receiptDetail : receiptDetails) {
            Long productId = receiptDetail.getProductId();
            try{
                String productName = productClient.getNameProductByID(productId);

            }catch (Exception e){
                e.printStackTrace();
            }

            ProductSummary summary = productSummaryMap.computeIfAbsent(productId, ProductSummary::new);
            summary.setQuantityReceiptDetail(summary.getQuantityReceiptDetail() + receiptDetail.getQuantity());
            summary.setPurchasePrice(summary.getPurchasePrice() + receiptDetail.getPurchasePrice());
            try{
                String productName = productClient.getNameProductByID(productId);
                summary.setProductName(productName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        for (DeliveryDetail deliveryDetail : deliveryDetails) {
            Long productId = deliveryDetail.getProductId();
            ProductSummary summary = productSummaryMap.computeIfAbsent(productId, ProductSummary::new);
            summary.setQuantityDeliveryDetail(summary.getQuantityDeliveryDetail() + deliveryDetail.getQuantity());
            summary.setPrice(summary.getPrice() + deliveryDetail.getPrice());
        }

        return new ArrayList<>(productSummaryMap.values());
    }



}
