package com.example.orderservice.service;

import com.example.orderservice.dto.InvoiceRequest;

import com.example.orderservice.entity.Invoice;

import java.util.List;
import java.util.Map;

public interface  IInvoiceService {

    Invoice createInvoice(InvoiceRequest invoiceRequest);

    public long getTotalPriceForCurrentWeek();
    public List<Invoice> getInvoicesByCustomerId(Long customerId);

    public long getTotalPriceForMonth(Long wareHouseId);
    public long getTotalPriceForMonth(int month, int year);

    Invoice getInvoiceById(Long id);

    public List<Invoice> getAllInvoices(Long warehouseId);

    public List<Invoice> getInvoicesByStatusAndWarehouseId(int status, Long warehouseId);

    public boolean updateInvoice(Long id, Invoice invoice);

    public boolean updateInvoiceStatus(Long invoiceId, Integer newStatus);
    public boolean updateInvoiceStatusPayment(Long invoiceId, Integer newStatus);

    public Invoice updateInvoice(Long invoiceId, InvoiceRequest invoiceRequest);

    boolean deleteInvoiceWithStatus_1(Long id);

    public Map<String, Object> getProductSalesSummary(int year);
    public Map<String, Object> getProductSalesSummary(int year, Long wareHouseId);


}
