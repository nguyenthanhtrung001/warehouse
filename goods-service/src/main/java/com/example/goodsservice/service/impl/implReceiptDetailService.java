package com.example.goodsservice.service.impl;

import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.ReceiptDetail;
import com.example.goodsservice.repository.ReceiptDetailRepository;
import com.example.goodsservice.repository.ReceiptRepository;
import com.example.goodsservice.service.IReceiptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implReceiptDetailService implements IReceiptDetailService {
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;
    @Autowired
    private ReceiptRepository receiptRepository;


    @Override
    public ReceiptDetail createReceiptDetail(ReceiptDetail receiptDetail) {
        return receiptDetailRepository.save(receiptDetail);
    }

    @Override
    public ReceiptDetail addReceiptDetail(Long receiptId, ReceiptDetail receiptDetail) {
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow(() -> new RuntimeException("Receipt not found"));
        receiptDetail.setReceipt(receipt);
        return receiptDetailRepository.save(receiptDetail);
    }

    @Override
    public List<ReceiptDetail> getReceiptDetails(Long receiptId) {
        return null;
    }

    @Override
    public Optional<ReceiptDetail> getReceiptDetailById(Long id) {
        return receiptDetailRepository.findById(id);
    }

    @Override
    public List<ReceiptDetail> getAllReceiptDetails() {
        return receiptDetailRepository.findAll();
    }

    @Override
    public boolean updateReceiptDetail(Long id, ReceiptDetail receiptDetail) {
        if (receiptDetailRepository.existsById(id)) {
            receiptDetail.setId(id);
            receiptDetailRepository.save(receiptDetail);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReceiptDetail(Long id) {
        if (receiptDetailRepository.existsById(id)) {
            receiptDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}