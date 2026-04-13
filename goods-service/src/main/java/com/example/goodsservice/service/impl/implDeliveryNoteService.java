package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.dto.*;
import com.example.goodsservice.dto.response.DeliverySummaryResponse;
import com.example.goodsservice.dto.response.OrderQuantity;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.Warehouse;
import com.example.goodsservice.repository.DeliveryDetailRepository;
import com.example.goodsservice.repository.DeliveryNoteRepository;
import com.example.goodsservice.service.IDeliveryDetailService;
import com.example.goodsservice.service.IDeliveryNoteService;
import com.example.goodsservice.service.IReceiptDetailService;
import com.example.goodsservice.service.IReceiptService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implDeliveryNoteService implements IDeliveryNoteService {
    // 1- xuatNcc, 2- xuatHuy 3- xuatChuyen
    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;
    @Autowired
    InventoryClient inventoryClient;
    @Autowired
    IReceiptService receiptService;
    @Autowired
    private IReceiptDetailService receiptDetailService;
    @Autowired
    private IDeliveryDetailService deliveryDetailService;


    @Override
    public DeliveryNote getDeliveryNoteById(Long id) {
        Optional<DeliveryNote> optionalDeliveryNote = deliveryNoteRepository.findById(id);
        return optionalDeliveryNote.orElse(null);
    }

    @Override
    public List<DeliveryNote> getAllDeliveryNotes(Long warehouseId) {
        return deliveryNoteRepository.findAllByTypeAndWarehouseId(1, warehouseId);
    }

    @Override
    public List<DeliveryNote> getAllDeliveryNotesCancel(Long warehouseId) {
        return deliveryNoteRepository.findAllByTypeAndWarehouseId(2, warehouseId);
    }

    @Override
    public List<DeliveryNote> getAllTransfer(Long warehouseId) {
        return deliveryNoteRepository.findAllByTypeAndWarehouseId(3, warehouseId);
    }
    @Override
    public List<DeliveryNote> getAllImportTransfer(Long warehouseId) {
        return deliveryNoteRepository.findAllImportTransferByTypeAndWarehouseId(3, warehouseId);
    }

    @Override
    public boolean updateDeliveryNote(Long id, DeliveryNote deliveryNoteDetails) {
        Optional<DeliveryNote> optionalDeliveryNote = deliveryNoteRepository.findById(id);
        if (optionalDeliveryNote.isPresent()) {
            DeliveryNote deliveryNote = optionalDeliveryNote.get();
            deliveryNote.setDeliveryDate(deliveryNoteDetails.getDeliveryDate());
            deliveryNote.setReceipt(deliveryNoteDetails.getReceipt());
            deliveryNote.setStatus(deliveryNoteDetails.getStatus());
            deliveryNoteRepository.save(deliveryNote);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteDeliveryNote(Long id) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id).orElse(null);
        if (deliveryNote.getStatus() != 1) return false;
        List<DeliveryDetail> deliveryDetails = deliveryDetailRepository.findByDeliveryNoteId(id);
        for (DeliveryDetail detail : deliveryDetails) {
            inventoryClient.updateQuantityForDeleteDelivery(detail.getBatchDetail_Id(),detail.getQuantity());
            deliveryDetailRepository.deleteById(detail.getId());
        }
        deliveryNoteRepository.deleteById(id);
        return true;
    }
    @Transactional
    public boolean cancelTransfer(Long id, String reason) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id).orElse(null);

        if (deliveryNote.getStatus() != 1) return false;

        deliveryNote.setReason(reason);
        deliveryNoteRepository.save(deliveryNote);

        List<DeliveryDetail> deliveryDetails = deliveryDetailRepository.findByDeliveryNoteId(id);
        for (DeliveryDetail detail : deliveryDetails) {
            inventoryClient.updateQuantityForDeleteDelivery(detail.getBatchDetail_Id(),detail.getQuantity());
           // deliveryDetailRepository.deleteById(detail.getId());
        }
        updateDeliveryNoteStatus(id,0);
        return true;
    }

    @Override
    public boolean updateDeliveryNoteStatus(Long id, Integer status) {
        Optional<DeliveryNote> optionalDeliveryNote = deliveryNoteRepository.findById(id);
        if (optionalDeliveryNote.isPresent()) {
            DeliveryNote deliveryNote = optionalDeliveryNote.get();
            deliveryNote.setStatus(status);
            deliveryNoteRepository.save(deliveryNote);
            return true;
        }
        return false;
    }

    @Transactional
    public DeliveryNote createDeliveryNoteWithDetails(Import_Export_Request importExportRequest) {
        // kiểm tra số lượng tồn kho của phiếu chuyển kho
        processProductQuantities( importExportRequest.getImport_Export_Details(),importExportRequest.getWarehouseId());

        DeliveryNote savedNote=null;

        try {
            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setDeliveryDate(LocalDateTime.now());
            Receipt receipt = new Receipt(importExportRequest.getReceipt());
            receiptService.updateReceiptStatus(receipt.getId(),2);
            deliveryNote.setReceipt( receipt );
            deliveryNote.setStatus(1);
            deliveryNote.setType(1);// xuất trả nhà cung cấp
            deliveryNote.setPrice(importExportRequest.getPrice());
            deliveryNote.setEmployeeId(importExportRequest.getEmployeeId());
            // set warehouse
            deliveryNote.setWarehouseSource( new Warehouse(importExportRequest.getWarehouseId()));
            savedNote = deliveryNoteRepository.save(deliveryNote);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng
        // getDeliveryNote: Tạo chi tiết phiếu xuất
        DeliveryNote result = setDeliveryNoteAndUpdateInventory( importExportRequest, savedNote);
        Integer quantityReceipt = receiptDetailService.getTotalQuantityByReceiptId(result.getReceipt().getId());
        Integer quantityNote = deliveryDetailService.getTotalQuantityByReceiptId(result.getReceipt().getId());
        if (quantityNote == quantityReceipt)
        {
            receiptService.updateReceiptStatus(result.getReceipt().getId(),3);
        }
        return result;
    }

    public boolean checkInventory(Long productId, Long warehouseId,Integer quantityProduct){
        Integer quantityInventory = inventoryClient.getQuantityByProductIdAndWarehouseId(productId,warehouseId);
        if (quantityProduct <= quantityInventory) {
            return true;
        }
        return false;
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
    public void processProductQuantities(List<Import_Export_DetailRequest> details, Long warehouseId) {
        List<ProductQuantity> productQuantities = getProductQuantities(details);

        for (ProductQuantity productQuantity : productQuantities) {
            boolean isAvailable = checkInventory(
                    productQuantity.getProductId(),
                    warehouseId,
                    productQuantity.getQuantity().intValue()
            );

            if (!isAvailable) {
                String errorMessage = "Sản phẩm với mã MH000" + productQuantity.getProductId() + " không đủ số lượng tồn kho.";
                System.out.println("Lỗi: "+errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
    }
    @Transactional
    public DeliveryNote createTransfer(Import_Export_Request importExportRequest) {

        // kiểm tra số lượng tồn kho của phiếu chuyển kho
        processProductQuantities( importExportRequest.getImport_Export_Details(),importExportRequest.getWarehouseId());

        DeliveryNote savedNote = null;

            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setDeliveryDate(LocalDateTime.now());
            deliveryNote.setStatus(1);
            deliveryNote.setType(3);// xuất chuyển kho
            deliveryNote.setPrice(0L);
            deliveryNote.setEmployeeId(importExportRequest.getEmployeeId());
            // set warehouse
            deliveryNote.setWarehouseSource( new Warehouse(importExportRequest.getWarehouseId()));
            deliveryNote.setWarehouseDestination(new Warehouse(importExportRequest.getWarehouseDestination()));

            savedNote = deliveryNoteRepository.save(deliveryNote);


        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng
        // getDeliveryNote: Tạo chi tiết phiếu xuất
        DeliveryNote result = setTransferAndUpdateInventory( importExportRequest, savedNote, importExportRequest.getWarehouseId());

        return result;
    }

    @Override
    public DeliverySummaryResponse getSummaryByTypeAndStatus(Integer type, Integer status) {
        // Tính tổng số phiếu DeliveryNote
        Long totalDeliveryNotes = deliveryNoteRepository.countByTypeAndStatus(type, status);

        // Tính tổng số lượng (quantity)
        Long totalQuantity = deliveryDetailRepository.sumQuantityByTypeAndStatus(type, status);

        // Trả về kết quả
        return new DeliverySummaryResponse(totalDeliveryNotes, totalQuantity != null ? totalQuantity : 0);
    }

    @Transactional
    public DeliveryNote createDeliveryNote_Delete_WithDetails(Import_Export_Request importExportRequest) {
        DeliveryNote savedNote=null;

        try {
            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setDeliveryDate(LocalDateTime.now());
            deliveryNote.setStatus(1);
            deliveryNote.setType(2);// xuất hủy hàng
            deliveryNote.setEmployeeId(importExportRequest.getEmployeeId());
            // set warehouse
            deliveryNote.setWarehouseSource(new Warehouse(importExportRequest.getWarehouseId()));
            savedNote = deliveryNoteRepository.save(deliveryNote);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng về 0

        return setDeliveryNoteAndUpdateInventory(importExportRequest, savedNote);
    }

    private DeliveryNote setDeliveryNoteAndUpdateInventory(Import_Export_Request importExportRequest, DeliveryNote savedNote) {

        for (Import_Export_DetailRequest detailRequest : importExportRequest.getImport_Export_Details()) {
           try {
               ResponseEntity<String> response = inventoryClient.updateDetailBathForDelivery(detailRequest.getBatchDetail_Id(), detailRequest.getQuantity() );
                System.out.println("Sl:"+detailRequest.getQuantity() );
           } catch (Exception e)
           {
               e.printStackTrace();
           }
            DeliveryDetail detail = new DeliveryDetail();
            detail.setDeliveryNote(savedNote);
            detail.setBatchDetail_Id(detailRequest.getBatchDetail_Id());
            detail.setProductId(detailRequest.getProduct_Id());
            detail.setQuantity(detailRequest.getQuantity());
            detail.setPrice(detailRequest.getPurchasePrice());
            System.out.println("Giá:"+detailRequest.getPurchasePrice() );
            deliveryDetailRepository.save(detail);

        }

        return savedNote;
    }
    private DeliveryNote setTransferAndUpdateInventory(Import_Export_Request importExportRequest, DeliveryNote savedNote, Long wareHouseId) {

        for (Import_Export_DetailRequest detailRequest : importExportRequest.getImport_Export_Details()) {

            Long productId = detailRequest.getProduct_Id();
            Integer quantity = detailRequest.getQuantity();


            List<OrderQuantity> transferQuantity = new ArrayList<>();
            try {
                transferQuantity = inventoryClient.updateQuantityTransfer(productId, quantity, wareHouseId);
                System.out.println("Sl:"+detailRequest.getQuantity() );

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            for( OrderQuantity transfer: transferQuantity){
                DeliveryDetail detail = new DeliveryDetail();
                detail.setDeliveryNote(savedNote);
                detail.setBatchDetail_Id(transfer.getBathDetail_Id());
                detail.setProductId(productId);
                detail.setQuantity(transfer.getQuantity());
                // chưa tạo giá xuất
                //detail.setPrice(detailRequest.getPurchasePrice());
                deliveryDetailRepository.save(detail);

            }

        }

        return savedNote;
    }


}
