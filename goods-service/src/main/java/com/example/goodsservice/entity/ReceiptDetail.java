package com.example.goodsservice.entity;

import jakarta.persistence.*;

@Entity
public class ReceiptDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    private Long batchDetail_Id;

    @Column(name = "purchase_price", nullable = false)
    private Long purchasePrice;

    @Column(name = "quantity", nullable = true)
    private Integer quantity;
    @Column(name = "ProductId", nullable = true)
    private Long ProductId;

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }

    public ReceiptDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Long getBatchDetail_Id() {
        return batchDetail_Id;
    }

    public void setBatchDetail_Id(Long batchDetail_Id) {
        this.batchDetail_Id = batchDetail_Id;
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

