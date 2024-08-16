package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.dto.*;
import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
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
import java.util.List;
import java.util.Optional;

@Service
public class implDeliveryNoteService implements IDeliveryNoteService {
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
    public DeliveryNote createDeliveryNote(DeliveryNote deliveryNote) {
        return deliveryNoteRepository.save(deliveryNote);
    }

    @Override
    public DeliveryNote getDeliveryNoteById(Long id) {
        Optional<DeliveryNote> optionalDeliveryNote = deliveryNoteRepository.findById(id);
        return optionalDeliveryNote.orElse(null);
    }

    @Override
    public List<DeliveryNote> getAllDeliveryNotes() {
        return deliveryNoteRepository.findAllByType(1);
    }
    @Override
    public List<DeliveryNote> getAllDeliveryNotesCancel() {
        return deliveryNoteRepository.findAllByType(2);
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
            savedNote = deliveryNoteRepository.save(deliveryNote);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng
        // getDeliveryNote: Tạo chi tiết phiếu xuất
        DeliveryNote result= getDeliveryNote( importExportRequest, savedNote);
        Integer quantityReceipt = receiptDetailService.getTotalQuantityByReceiptId(result.getReceipt().getId());
        Integer quantityNote = deliveryDetailService.getTotalQuantityByReceiptId(result.getReceipt().getId());
        if (quantityNote == quantityReceipt)
        {
            receiptService.updateReceiptStatus(result.getReceipt().getId(),3);
        }
        return result;
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
            savedNote = deliveryNoteRepository.save(deliveryNote);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng về 0

        return getDeliveryNote(importExportRequest, savedNote);
    }

    private DeliveryNote getDeliveryNote(Import_Export_Request importExportRequest, DeliveryNote savedNote) {

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


}
