package com.example.orderservice.service;

import com.example.orderservice.dto.InvoiceRequest;

import com.example.orderservice.entity.Invoice;

import java.util.List;
import java.util.Map;

public interface  IInvoiceService {

    Invoice createInvoice(InvoiceRequest invoiceRequest);

    public long getTotalPriceForCurrentWeek();

    public long getTotalPriceForCurrentMonth();

    Invoice getInvoiceById(Long id);

    List<Invoice> getAllInvoices();

    public List<Invoice> getAllInvoicesWithStatus(Integer status);

    public boolean updateInvoice(Long id, Invoice invoice);

    public boolean updateInvoiceStatus(Long invoiceId, Integer newStatus);

    public Invoice updateInvoice(Long invoiceId, InvoiceRequest invoiceRequest);

    boolean deleteInvoiceWithStatus_1(Long id);

    public Map<String, Object> getProductSalesSummary(int year);
}
