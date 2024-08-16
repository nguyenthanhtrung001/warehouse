package com.example.orderservice.service;

import com.example.orderservice.dto.response.InvoiceDetailResponse;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.ReturnDetail;

import java.util.List;

public interface IReturnDetailService {
    ReturnDetail createReturnDetail(ReturnDetail returnDetail);

    ReturnDetail getReturnDetailById(Long id);
    public List<InvoiceDetailResponse> getReturnDetailsByInvoiceId(Long invoiceId);

    List<ReturnDetail> getAllReturnDetails();

    boolean updateReturnDetail(Long id, ReturnDetail returnDetail);

    boolean deleteReturnDetail(Long id);
    public List<ProductQuantity> getProductQuantitiesForCurrentMonth();
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year);
}
