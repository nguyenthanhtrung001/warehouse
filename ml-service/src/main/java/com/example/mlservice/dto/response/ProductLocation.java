package com.example.mlservice.dto.response;

public class ProductLocation {
    ProductResponse product;
    Long quantity;

    public ProductLocation() {
    }

    public ProductLocation(ProductResponse product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
