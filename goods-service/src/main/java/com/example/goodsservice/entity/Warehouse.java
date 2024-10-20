package com.example.goodsservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Warehouse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_name", nullable = false, length = 150)
    private String warehouseName;

    @Column(name = "location", nullable = false, length = 250)
    private String location;

    @Column(name = "capacity")
    private Double capacity;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "status")
    private Integer status;

    @Column(name = "note", length = 500)
    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "warehouse")
    private List<Receipt> receipts;

    @JsonIgnore
    @OneToMany(mappedBy = "warehouseSource")
    private List<DeliveryNote> outgoingDeliveries;

    @JsonIgnore
    @OneToMany(mappedBy = "warehouseDestination")
    private List<DeliveryNote> incomingDeliveries;

    // Constructors, getters, and setters

    public Warehouse() {
    }

    public Warehouse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public List<DeliveryNote> getOutgoingDeliveries() {
        return outgoingDeliveries;
    }

    public void setOutgoingDeliveries(List<DeliveryNote> outgoingDeliveries) {
        this.outgoingDeliveries = outgoingDeliveries;
    }

    public List<DeliveryNote> getIncomingDeliveries() {
        return incomingDeliveries;
    }

    public void setIncomingDeliveries(List<DeliveryNote> incomingDeliveries) {
        this.incomingDeliveries = incomingDeliveries;
    }
}
