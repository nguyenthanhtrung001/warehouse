package com.example.goodsservice.entity;

import jakarta.persistence.*;

@Entity
public class DeliveryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private DeliveryNote deliveryNote;

    private Long batchDetail_Id;
    @Column(name = "ProductId", nullable = true)
    private Long ProductId;

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }

    public Long getBatchDetail_Id() {
        return batchDetail_Id;
    }

    public void setBatchDetail_Id(Long batchDetail_Id) {
        this.batchDetail_Id = batchDetail_Id;
    }
// Add BatchDetail entity if needed
//    @ManyToOne
//    @JoinColumn(name = "batch_detail_id", nullable = false)
//    private BatchDetail batchDetail;

    private Integer quantity;

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long price) {
        Price = price;
    }

    private Long Price;

    public DeliveryDetail() {
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeliveryNote getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

//    public BatchDetail getBatchDetail() {
//        return batchDetail;
//    }
//
//    public void setBatchDetail(BatchDetail batchDetail) {
//        this.batchDetail = batchDetail;
//    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
