package com.example.inventoryservice.dto;

import com.example.inventoryservice.entity.BatchDetail;

public class InventoryCheckDetailRequest {

    private Long batchDetail;
    private Integer inventory;
    private Integer actualQuantity;
    private Integer quantityDiscrepancy;

    // Getters and setters


    public Long getBatchDetail() {
        return batchDetail;
    }

    public void setBatchDetail(Long batchDetail) {
        this.batchDetail = batchDetail;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public Integer getQuantityDiscrepancy() {
        return quantityDiscrepancy;
    }

    public void setQuantityDiscrepancy(Integer quantityDiscrepancy) {
        this.quantityDiscrepancy = quantityDiscrepancy;
    }
}
