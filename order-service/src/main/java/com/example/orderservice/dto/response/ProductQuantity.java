package com.example.orderservice.dto.response;

public class ProductQuantity {

    private Long productId;
    private Long quantity;
    private String productName;

    // Constructor
    public ProductQuantity(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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