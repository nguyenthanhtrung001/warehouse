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
import com.example.goodsservice.repository.ReceiptDetailRepository;
import com.example.goodsservice.repository.ReceiptRepository;
import com.example.goodsservice.service.IDeliveryDetailService;
import com.example.goodsservice.service.IReceiptDetailService;
import com.example.goodsservice.service.IReceiptService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Receipt createReceipt(Receipt receipt) {
        // Gọi api tạo lô hàng trước
        return receiptRepository.save(receipt);
    }

    @Override
    public ReceiptSummary getReceiptSummaryForCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX);

        List<Receipt> receipts = receiptRepository.findAllReceiptsInMonth(startOfMonth, endOfMonth);

        long totalReceipts = receipts.size();
        long totalPurchasePrice = receipts.stream()
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
    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAllByStatusNotZero();
    }

    @Override
    public List<Receipt> getAllReceiptsForReturn() {
        return receiptRepository.findAllByStatusNotZeroAndNotThree();
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

    @Transactional
    public Receipt createReceiptWithDetails(Import_Export_Request importExportRequest) {

        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng
        BathRequest bathRequest = bathMapper.toBathRequest(importExportRequest);
        bathRequest.setStatus(1);
        BathRequest bath= new BathRequest();
        try{
             bath = inventoryClient.createBath(bathRequest);

        }catch(Exception e){
            e.printStackTrace();
        }

         /*   System.out.println("ID Name:"+bath.getBatchName());
        System.out.println("ID bath:"+bath.getId());*/

        Receipt savedReceipt = null;
        Receipt receipt = new Receipt();
       try {
           receipt.setReceiptDate(LocalDateTime.now());

           Supplier supplier = new Supplier(importExportRequest.getSupplier());
           receipt.setSupplier(supplier);
           receipt.setStatus(1);
           receipt.setPurchasePrice(importExportRequest.getPrice());
           // API lấy thông tin đăng nhập để set ID Nhân viên
            receipt.setEmployeeId(importExportRequest.getEmployeeId());
           savedReceipt = receiptRepository.save(receipt);

       }catch (Exception e)
       {
           e.printStackTrace();
       }

        for (Import_Export_DetailRequest detailRequest : importExportRequest.getImport_Export_Details()) {


            System.out.println("ID Name abc:"+detailRequest.getProduct_Id());
            Bath bathTmp =new Bath();
            bathTmp.setId(bath.getId());
            Location location = new Location();
            location.setId(importExportRequest.getLocation());
            BathDetailRequest bathDetailRequest = bathDetailMapper.toBathDetailRequest(detailRequest);
            bathDetailRequest.setBatch(bathTmp);
            bathDetailRequest.setLocation(location);
            BathDetailRequest bathDetail = inventoryClient.createDetailBath(bathDetailRequest);


            ReceiptDetail detail = new ReceiptDetail();
            detail.setReceipt(savedReceipt);

            detail.setPurchasePrice(detailRequest.getPurchasePrice());
            detail.setQuantity(detailRequest.getQuantity());
            detail.setBatchDetail_Id(bathDetail.getId());
            detail.setProductId(detailRequest.getProduct_Id());


            receiptDetailRepository.save(detail);
        }

        return savedReceipt;
    }
    public static List<ProductQuantity> getLargestList(List<List<ProductQuantity>> lists) {
        return lists.stream()
                .max(Comparator.comparingInt(List::size))
                .orElse(new ArrayList<>());
    }

    @Override
    public List<ReportImportExport> createReportImportExport(Integer month, Integer year) {
        List<ReportImportExport> reportImportExports = new ArrayList<>();

        // Khởi tạo các danh sách với giá trị mặc định nếu các dịch vụ trả về null
        List<ProductQuantity> list_export_return_order = Optional.ofNullable(orderClient.getProductQuantity_import_order(month, year))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_import_check_inventory = Optional.ofNullable(inventoryClient.getProductQuantity_import_check_inventory(month, year))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_import_receipt = Optional.ofNullable(receiptDetailService.getProductQuantitiesForMonthYear(month, year))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_export_cancel = Optional.ofNullable(deliveryDetailService.getProductQuantitiesForMonthYearAndType(month, year, 2))
                .orElse(new ArrayList<>());


        List<ProductQuantity> list_export_return_receipt = Optional.ofNullable(deliveryDetailService.getProductQuantitiesForMonthYearAndType(month, year, 1))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_export_check_inventory = Optional.ofNullable(inventoryClient.getProductQuantity_export_check_inventory(month, year))
                .orElse(new ArrayList<>());

        List<ProductQuantity> list_import_order = Optional.ofNullable(orderClient.getProductQuantity_export_return_order(month, year))
                .orElse(new ArrayList<>());

        List<List<ProductQuantity>> allLists = Arrays.asList(
                list_import_order,
                list_import_check_inventory,
                list_import_receipt,
                list_export_return_receipt,
                list_export_cancel,
                list_export_check_inventory,
                list_export_return_order
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
                        + reportImportExport.getImport_supplier();
                        long b = reportImportExport.getExport_cancel()
                        + reportImportExport.getExport_supplier()
                        + reportImportExport.getExport_order()
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

    @Override
    public List<ProductSummary> getProductSummaryBySupplierId(Long supplierId) {
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findByReceipt_Supplier_Id(supplierId);
        List<DeliveryDetail> deliveryDetails = deliveryDetailRepository.findByDeliveryNote_Receipt_Supplier_Id(supplierId);

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
