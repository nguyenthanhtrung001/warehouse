package com.example.orderservice.dto;

import com.example.orderservice.entity.Customer;

import java.util.List;

public class InvoiceRequest {
    private Customer customer;
    private List<InvoiceDetailRequest> invoiceDetails;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<InvoiceDetailRequest> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetailRequest> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }
}
