package com.example.mlservice.dto.response;


import java.time.LocalDateTime;
import java.util.List;


public class Receipt {
    private Long id;
    private LocalDateTime receiptDate;
    private Supplier supplier;
    private Warehouse warehouse;
    private Warehouse warehouseTransfer;
    private List<DeliveryNote> deliveryNotes;
    private Long employeeId;
    private Integer status;
    private Long purchasePrice;

    // Constructors
    public Receipt() {}

    public Receipt(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Warehouse getWarehouseTransfer() {
        return warehouseTransfer;
    }

    public void setWarehouseTransfer(Warehouse warehouseTransfer) {
        this.warehouseTransfer = warehouseTransfer;
    }

    public List<DeliveryNote> getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(List<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
