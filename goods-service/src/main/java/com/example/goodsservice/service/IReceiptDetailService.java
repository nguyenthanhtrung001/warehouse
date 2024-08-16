package com.example.goodsservice.service;

import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.dto.response.ReceiptDetailResponse;
import com.example.goodsservice.entity.ReceiptDetail;

import java.util.List;
import java.util.Optional;

public interface IReceiptDetailService {

    ReceiptDetail createReceiptDetail(ReceiptDetail receiptDetail);

    public List<ProductQuantity> getProductQuantitiesForCurrentMonth();
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year);
    ReceiptDetail addReceiptDetail(Long receiptId, ReceiptDetail receiptDetail);

    public List<ReceiptDetailResponse> getReceiptDetails(Long receiptId);
    public List<ReceiptDetail> getReceiptDetailsWithReceiptId(Long receiptId);
    public List<ReceiptDetailResponse> getReceiptDetailsWithUpdateQuantity(Long receiptId);
    Optional<ReceiptDetail> getReceiptDetailById(Long id);

    List<ReceiptDetail> getAllReceiptDetails();

    boolean updateReceiptDetail(Long id, ReceiptDetail receiptDetail);

    boolean deleteReceiptDetail(Long id);
    public Integer getTotalQuantityByReceiptId(Long receiptId);
    boolean existsByProductId(Long productId);
}
