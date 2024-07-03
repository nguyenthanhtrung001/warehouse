package com.example.goodsservice.dto;

import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.Supplier;

import java.util.List;

public class Import_Export_Request {

    private Receipt receipt;
    private Supplier supplier;
    private Integer status;
    private Long price;
    private Long employeeId;
    private List<Import_Export_DetailRequest> import_Export_Details;

    public Import_Export_Request() {
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public List<Import_Export_DetailRequest> getImport_Export_Details() {
        return import_Export_Details;
    }

    public void setImport_Export_Details(List<Import_Export_DetailRequest> import_Export_Details) {
        this.import_Export_Details = import_Export_Details;
    }
}
