package com.example.orderservice.service;

import com.example.orderservice.entity.InvoiceDetail;

import java.util.List;

public interface IInvoiceDetailService {
    InvoiceDetail createInvoiceDetail(InvoiceDetail invoiceDetail);

    InvoiceDetail getInvoiceDetailById(Long id);

    List<InvoiceDetail> getAllInvoiceDetails();

    boolean updateInvoiceDetail(Long id, InvoiceDetail invoiceDetail);

    boolean deleteInvoiceDetail(Long id);
}
