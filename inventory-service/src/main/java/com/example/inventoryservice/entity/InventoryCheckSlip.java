package com.example.inventoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "inventory_check_slips")
public class InventoryCheckSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inventory_check_time", nullable = false)
    private Date inventoryCheckTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "inventory_balancing_date")
    private Date inventoryBalancingDate;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "total_discrepancy", nullable = false)
    private Double totalDiscrepancy;

    @Column(name = "quantity_discrepancy_increase", nullable = false)
    private Integer quantityDiscrepancyIncrease;

    @Column(name = "quantity_discrepancy_decrease", nullable = false)
    private Integer quantityDiscrepancyDecrease;

    @Column(name = "notes")
    private String notes;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;
    @JsonIgnore
    @OneToMany(mappedBy = "inventoryCheckSlip", cascade = CascadeType.ALL)
    private List<InventoryCheckDetail> inventoryCheckDetails;

    // Constructors, getters and setters

    public InventoryCheckSlip() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInventoryCheckTime() {
        return inventoryCheckTime;
    }

    public void setInventoryCheckTime(Date inventoryCheckTime) {
        this.inventoryCheckTime = inventoryCheckTime;
    }

    public Date getInventoryBalancingDate() {
        return inventoryBalancingDate;
    }

    public void setInventoryBalancingDate(Date inventoryBalancingDate) {
        this.inventoryBalancingDate = inventoryBalancingDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getTotalDiscrepancy() {
        return totalDiscrepancy;
    }

    public void setTotalDiscrepancy(Double totalDiscrepancy) {
        this.totalDiscrepancy = totalDiscrepancy;
    }

    public Integer getQuantityDiscrepancyIncrease() {
        return quantityDiscrepancyIncrease;
    }

    public void setQuantityDiscrepancyIncrease(Integer quantityDiscrepancyIncrease) {
        this.quantityDiscrepancyIncrease = quantityDiscrepancyIncrease;
    }

    public Integer getQuantityDiscrepancyDecrease() {
        return quantityDiscrepancyDecrease;
    }

    public void setQuantityDiscrepancyDecrease(Integer quantityDiscrepancyDecrease) {
        this.quantityDiscrepancyDecrease = quantityDiscrepancyDecrease;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<InventoryCheckDetail> getInventoryCheckDetails() {
        return inventoryCheckDetails;
    }

    public void setInventoryCheckDetails(List<InventoryCheckDetail> inventoryCheckDetails) {
        this.inventoryCheckDetails = inventoryCheckDetails;
    }
}
