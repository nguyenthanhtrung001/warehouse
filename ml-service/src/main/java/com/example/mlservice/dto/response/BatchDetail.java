package com.example.mlservice.dto.response;

import jakarta.persistence.*;

public class BatchDetail {
    private Long id;

    private Batch batch;

    private Long productId;


    private Integer quantity;


    private Location location;

    public BatchDetail() {}

    public BatchDetail(Long id) {
        this.id = id;
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
