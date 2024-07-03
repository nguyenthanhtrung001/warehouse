package com.example.goodsservice.service.impl;

import com.example.goodsservice.dto.Import_Export_DetailRequest;
import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.ReceiptDetail;
import com.example.goodsservice.repository.ReceiptDetailRepository;
import com.example.goodsservice.repository.ReceiptRepository;
import com.example.goodsservice.service.IReceiptService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class implReceiptService implements IReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;

    @Override
    public Receipt createReceipt(Receipt receipt) {
        // Gọi api tạo lô hàng trước
        return receiptRepository.save(receipt);
    }

    @Override
    public Receipt getReceiptById(Long id) {
        Optional<Receipt> receipt = receiptRepository.findById(id);
        return receipt.orElse(null);
    }

    @Override
    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
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
    public boolean deleteReceipt(Long id) {
        if (receiptRepository.existsById(id)) {
            receiptRepository.deleteById(id);
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
        Receipt savedReceipt=null;
       try {
           Receipt receipt = new Receipt();
           receipt.setReceiptDate(LocalDate.now());
           receipt.setSupplier(importExportRequest.getSupplier());
           receipt.setStatus(1);
           receipt.setPurchasePrice(importExportRequest.getPrice());
           receipt.setEmployeeId(importExportRequest.getEmployeeId());
           savedReceipt = receiptRepository.save(receipt);
       }catch (Exception e)
       {
           e.printStackTrace();
       }

        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng

        for (Import_Export_DetailRequest detailRequest : importExportRequest.getImport_Export_Details()) {
            ReceiptDetail detail = new ReceiptDetail();
            detail.setReceipt(savedReceipt);
            detail.setBatchDetail_Id(detailRequest.getBatchDetail_Id());
            detail.setPurchasePrice(detailRequest.getPurchasePrice());
            detail.setQuantity(detailRequest.getQuantity());

            receiptDetailRepository.save(detail);
        }

        return savedReceipt;
    }
}
