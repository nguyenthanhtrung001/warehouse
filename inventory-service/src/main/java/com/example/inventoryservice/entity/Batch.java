package com.example.inventoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "batches")
@DynamicInsert
@DynamicUpdate
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_name", nullable = false)
    private String batchName;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private Integer status;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @JsonIgnore
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<BatchDetail> batchDetails;

    // Default constructor
    public Batch() {
    }

    // Parameterized constructor
    public Batch(Long id, String batchName, Date expiryDate, String note, Integer status, Long warehouseId) {
        this.id = id;
        this.batchName = batchName;
        this.expiryDate = expiryDate;
        this.note = note;
        this.status = status;
        this.warehouseId = warehouseId;
    }

    public Batch(Long warehouseId, Object o) {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<BatchDetail> getBatchDetails() {
        return batchDetails;
    }

    public void setBatchDetails(List<BatchDetail> batchDetails) {
        this.batchDetails = batchDetails;
    }
}
