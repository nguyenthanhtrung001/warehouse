package com.example.orderservice.service;

import com.example.orderservice.dto.InvoiceRequest;
import com.example.orderservice.entity.Invoice;

import java.util.List;

public interface  IInvoiceService {
    Invoice createInvoice(Invoice invoice);
    Invoice createInvoice(InvoiceRequest invoiceRequest);

    Invoice getInvoiceById(Long id);

    List<Invoice> getAllInvoices();

    boolean updateInvoice(Long id, Invoice invoice);
    public Invoice updateInvoice(Long invoiceId, InvoiceRequest invoiceRequest);

    boolean deleteInvoice(Long id);
}
