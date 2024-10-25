package com.example.goodsservice.service;

import com.example.goodsservice.dto.response.NoteDetailResponse;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.dto.response.ReceiptDetailResponse;
import com.example.goodsservice.entity.DeliveryDetail;

import java.util.List;
import java.util.Optional;

public interface IDeliveryDetailService {

    boolean deleteDeliveryDetail(Long id);
    public List<NoteDetailResponse> getNoteDetails(Long node);
    public List<ProductQuantity> getProductQuantitiesForCurrentMonthAndType(int type);
    public List<ProductQuantity> getProductQuantitiesForMonthYearAndType(int month, int year, int type);
    public List<ProductQuantity> getProductQuantitiesForMonthYearAndType(int month, int year, int type, Long warehouseId);
    public Integer getTotalQuantity(Long receiptId, Long batchDetailId);
    public Integer getTotalQuantityByReceiptId(Long receiptId);
}
