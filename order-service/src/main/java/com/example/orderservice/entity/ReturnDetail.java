package com.example.orderservice.entity;
import jakarta.persistence.*;
@Entity
public class ReturnDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "return_note_id")
    private ReturnNote returnNote;

    @Column(name = "product_id")
    private Long productId;

    private Integer quantity;
    private Long purchasePrice;

    public ReturnDetail() {
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public ReturnNote getReturnNote() {
        return returnNote;
    }

    public void setReturnNote(ReturnNote returnNote) {
        this.returnNote = returnNote;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}