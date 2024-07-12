package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.dto.*;
import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.ReceiptDetail;
import com.example.goodsservice.repository.DeliveryDetailRepository;
import com.example.goodsservice.repository.DeliveryNoteRepository;
import com.example.goodsservice.repository.ReceiptDetailRepository;
import com.example.goodsservice.service.IDeliveryNoteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class implDeliveryNoteService implements IDeliveryNoteService {
    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;
    @Autowired
    InventoryClient inventoryClient;


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
        return deliveryNoteRepository.findAll();
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

    @Override
    public boolean deleteDeliveryNote(Long id) {
        if (deliveryNoteRepository.existsById(id)) {
            deliveryNoteRepository.deleteById(id);
            return true;
        }
        return false;
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
        return getDeliveryNote( importExportRequest, savedNote);
    }


    @Transactional
    public DeliveryNote createDeliveryNote_Delete_WithDetails(Import_Export_Request importExportRequest) {
        DeliveryNote savedNote=null;

        try {
            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setDeliveryDate(LocalDateTime.now());
            Receipt receipt = new Receipt(importExportRequest.getReceipt());
            deliveryNote.setReceipt(receipt);
            deliveryNote.setStatus(1);
            deliveryNote.setType(2);// xuất hủy hàng
            System.out.println(deliveryNote.getType());
            deliveryNote.setPrice(importExportRequest.getPrice());
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
               ResponseEntity<String> response = inventoryClient.updateDetailBath(detailRequest.getBatchDetail_Id(), detailRequest.getQuantity() );

           } catch (Exception e)
           {
               e.printStackTrace();
           }

            DeliveryDetail detail = new DeliveryDetail();
            detail.setDeliveryNote(savedNote);
            detail.setBatchDetail_Id(detailRequest.getProduct_Id());
            detail.setQuantity(detailRequest.getQuantity());
            deliveryDetailRepository.save(detail);

        }

        return savedNote;
    }

}
