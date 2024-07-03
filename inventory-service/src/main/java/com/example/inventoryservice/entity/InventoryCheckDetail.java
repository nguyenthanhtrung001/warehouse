package com.example.inventoryservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_check_details")
public class InventoryCheckDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_check_slip_id", nullable = false)
    private InventoryCheckSlip inventoryCheckSlip;
    @ManyToOne
    @JoinColumn(name = "batch_detail_id", nullable = false)
    private BatchDetail batchDetail;

    @Column(name = "inventory", nullable = false)
    private Integer inventory;

    @Column(name = "actual_quantity", nullable = false)
    private Integer actualQuantity;

    @Column(name = "quantity_discrepancy", nullable = false)
    private Integer quantityDiscrepancy;

    // Constructors, getters and setters

    public InventoryCheckDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryCheckSlip getInventoryCheckSlip() {
        return inventoryCheckSlip;
    }

    public void setInventoryCheckSlip(InventoryCheckSlip inventoryCheckSlip) {
        this.inventoryCheckSlip = inventoryCheckSlip;
    }

    public BatchDetail getBatchDetail() {
        return batchDetail;
    }

    public void setBatchDetail(BatchDetail batchDetail) {
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
