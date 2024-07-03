package com.example.orderservice.entity;
import jakarta.persistence.*;
@Entity
public class ReturnDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "return_note_id")
    private Long returnNote;

    @Column(name = "product_id")
    private Long productId;

    private Integer quantity;

    public ReturnDetail() {
    }

    public Long getReturnNote() {
        return returnNote;
    }

    public void setReturnNote(Long returnNote) {
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