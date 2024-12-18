package com.example.orderservice.dto;


import com.example.orderservice.entity.Customer;

import java.util.List;

public class InvoiceRequest {
    private Long customer;
    private Long price;
    private Long employeeId;
    private List<InvoiceDetailRequest> order_Details;
    private String note;
    private Long warehouseId;
    private Long  contactId;

    public InvoiceRequest() {
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<InvoiceDetailRequest> getOrder_Details() {
        return order_Details;
    }

    public void setOrder_Details(List<InvoiceDetailRequest> order_Details) {
        this.order_Details = order_Details;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }}
