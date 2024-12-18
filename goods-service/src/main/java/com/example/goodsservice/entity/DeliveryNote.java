package com.example.goodsservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DeliveryNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deliveryDate;

    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = true)
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "warehouse_source_id", nullable = false)
    private Warehouse warehouseSource;

    @ManyToOne
    @JoinColumn(name = "warehouse_destination_id", nullable = true)
    private Warehouse warehouseDestination;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    private Integer type;
    private Integer status;
    private Long price;

    private String reason;

    // Constructors, getters, and setters

    public DeliveryNote() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Warehouse getWarehouseSource() {
        return warehouseSource;
    }

    public void setWarehouseSource(Warehouse warehouseSource) {
        this.warehouseSource = warehouseSource;
    }

    public Warehouse getWarehouseDestination() {
        return warehouseDestination;
    }

    public void setWarehouseDestination(Warehouse warehouseDestination) {
        this.warehouseDestination = warehouseDestination;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
}
