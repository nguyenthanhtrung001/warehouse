package com.example.inventoryservice.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class InventoryCheckSlipRequest {

    private LocalDateTime inventoryCheckTime;
    private LocalDateTime inventoryBalancingDate;
    private Integer status;
    private Long totalDiscrepancy;
    private Integer quantityDiscrepancyIncrease;
    private Integer quantityDiscrepancyDecrease;
    private String notes;
    private Long employeeId;
    private List<InventoryCheckDetailRequest> inventoryCheckDetails;

    // Getters and setters

    public LocalDateTime getInventoryCheckTime() {
        return inventoryCheckTime;
    }

    public void setInventoryCheckTime(LocalDateTime inventoryCheckTime) {
        this.inventoryCheckTime = inventoryCheckTime;
    }

    public LocalDateTime getInventoryBalancingDate() {
        return inventoryBalancingDate;
    }

    public void setInventoryBalancingDate(LocalDateTime inventoryBalancingDate) {
        this.inventoryBalancingDate = inventoryBalancingDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTotalDiscrepancy() {
        return totalDiscrepancy;
    }

    public void setTotalDiscrepancy(Long totalDiscrepancy) {
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

    public List<InventoryCheckDetailRequest> getInventoryCheckDetails() {
        return inventoryCheckDetails;
    }

    public void setInventoryCheckDetails(List<InventoryCheckDetailRequest> inventoryCheckDetails) {
        this.inventoryCheckDetails = inventoryCheckDetails;
    }
}
