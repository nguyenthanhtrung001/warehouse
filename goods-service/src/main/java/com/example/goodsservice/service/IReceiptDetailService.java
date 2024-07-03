package com.example.goodsservice.service;

import com.example.goodsservice.entity.ReceiptDetail;

import java.util.List;
import java.util.Optional;

public interface IReceiptDetailService {

    ReceiptDetail createReceiptDetail(ReceiptDetail receiptDetail);


    ReceiptDetail addReceiptDetail(Long receiptId, ReceiptDetail receiptDetail);

    public List<ReceiptDetail> getReceiptDetails(Long receiptId);
    Optional<ReceiptDetail> getReceiptDetailById(Long id);

    List<ReceiptDetail> getAllReceiptDetails();

    boolean updateReceiptDetail(Long id, ReceiptDetail receiptDetail);

    boolean deleteReceiptDetail(Long id);
}
