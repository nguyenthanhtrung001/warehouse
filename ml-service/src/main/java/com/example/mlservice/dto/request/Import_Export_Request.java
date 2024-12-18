package com.example.mlservice.dto.request;


import java.util.Date;
import java.util.List;

public class Import_Export_Request {

    private Long receipt;
    private Long deliveryNote;
    private Long supplier;
    private Integer status;
    private Long price;
    private Long employeeId;

    private Long warehouseId;
    private List<Import_Export_DetailRequest> import_Export_Details;
    // Lô hàng
    private String batchName;

    private Date expiryDate;
    private String note;
    // Chi tiết lô hàng
    private Long batchID;

    private Integer quantity;
    private Long location;
    private Long warehouseDestination;

    public Long getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(Long deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getBatchID() {
        return batchID;
    }

    public Long getWarehouseDestination() {
        return warehouseDestination;
    }

    public void setWarehouseDestination(Long warehouseDestination) {
        this.warehouseDestination = warehouseDestination;
    }

    public void setBatchID(Long batchID) {
        this.batchID = batchID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public Import_Export_Request() {
    }

    public Long getReceipt() {
        return receipt;
    }

    public void setReceipt(Long receipt) {
        this.receipt = receipt;
    }

    public Long getSupplier() {
        return supplier;
    }

    public void setSupplier(Long supplier) {
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
