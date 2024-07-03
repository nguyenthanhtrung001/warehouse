package com.example.goodsservice.service.impl;

import com.example.goodsservice.dto.Import_Export_DetailRequest;
import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.repository.DeliveryDetailRepository;
import com.example.goodsservice.repository.DeliveryNoteRepository;
import com.example.goodsservice.service.IDeliveryNoteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class implDeliveryNoteService implements IDeliveryNoteService {
    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

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
            deliveryNote.setDeliveryDate(LocalDate.now());
            deliveryNote.setReceipt(importExportRequest.getReceipt());
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

        return getDeliveryNote(importExportRequest, savedNote);
    }

    @Transactional
    public DeliveryNote createDeliveryNote_Delete_WithDetails(Import_Export_Request importExportRequest) {
        DeliveryNote savedNote=null;
        try {
            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setDeliveryDate(LocalDate.now());
            deliveryNote.setReceipt(importExportRequest.getReceipt());
            deliveryNote.setStatus(1);
            deliveryNote.setType(2);// xuất trả nhà cung cấp
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
            DeliveryDetail detail = new DeliveryDetail();
            detail.setDeliveryNote(savedNote);
            detail.setBatchDetail_Id(detailRequest.getBatchDetail_Id());
            detail.setQuantity(detailRequest.getQuantity());

            deliveryDetailRepository.save(detail);
        }

        return savedNote;
    }

}
