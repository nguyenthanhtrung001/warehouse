package com.example.inventoryservice.dto;

import java.util.Date;
import java.util.List;

public class InventoryCheckSlipRequest {

    private Date inventoryCheckTime;
    private Date inventoryBalancingDate;
    private Integer status;
    private Double totalDiscrepancy;
    private Integer quantityDiscrepancyIncrease;
    private Integer quantityDiscrepancyDecrease;
    private String notes;
    private Long employeeId;
    private List<InventoryCheckDetailRequest> inventoryCheckDetails;

    // Getters and setters

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

    public List<InventoryCheckDetailRequest> getInventoryCheckDetails() {
        return inventoryCheckDetails;
    }

    public void setInventoryCheckDetails(List<InventoryCheckDetailRequest> inventoryCheckDetails) {
        this.inventoryCheckDetails = inventoryCheckDetails;
    }
}
