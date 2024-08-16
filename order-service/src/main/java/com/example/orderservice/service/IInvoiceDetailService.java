package com.example.orderservice.service;

import com.example.orderservice.dto.response.InvoiceDetailResponse;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.InvoiceDetail;

import java.util.List;

public interface IInvoiceDetailService {
    InvoiceDetail createInvoiceDetail(InvoiceDetail invoiceDetail);

    InvoiceDetail getInvoiceDetailById(Long id);

    List<InvoiceDetail> getAllInvoiceDetails();
    List<InvoiceDetailResponse> getInvoiceDetailsByInvoiceId(Long invoiceId);
    List<InvoiceDetail> getDetailsByInvoiceId(Long invoiceId);

    boolean updateInvoiceDetail(Long id, InvoiceDetail invoiceDetail);

    boolean deleteInvoiceDetail(Long id);
    public List<ProductQuantity> getProductQuantities(Integer limit);
    public List<ProductQuantity> getProductQuantitiesForCurrentMonth();
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year);
    public ProductQuantity getProductQuantitiesForLastThreeMonths(Long productId);
    public String getNoteReturnByInvoiceIdAndProductId(Long invoiceId, Long productId);

}
