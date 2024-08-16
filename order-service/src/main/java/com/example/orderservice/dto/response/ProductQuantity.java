package com.example.orderservice.dto.response;

public class ProductQuantity {

    private Long productId;
    private Long quantity;

    // Constructor
    public ProductQuantity(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductQuantity() {

    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}