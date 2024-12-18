package com.example.mlservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


public class InventoryCheckSlip {
    private Long id;
    private LocalDateTime inventoryCheckTime;
    private LocalDateTime inventoryBalancingDate;
    private Integer totalDiscrepancy;
    private Integer quantityDiscrepancyIncrease;
    private Integer quantityDiscrepancyDecrease;
    private String notes;
    private Long employeeId;
    private Long warehouseId;

    // Constructors, getters and setters

    public InventoryCheckSlip() {
    }

    public InventoryCheckSlip(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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



    public Integer getTotalDiscrepancy() {
        return totalDiscrepancy;
    }

    public void setTotalDiscrepancy(Integer totalDiscrepancy) {
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

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }


}
