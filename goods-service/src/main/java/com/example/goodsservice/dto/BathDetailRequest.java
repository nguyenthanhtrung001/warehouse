package com.example.goodsservice.dto;

public class BathDetailRequest {

    private Long id;
    private Bath batch;

    private Long productId;
    private Integer quantity;
    private Location location;

    public BathDetailRequest() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bath getBatch() {
        return batch;
    }

    public void setBatch(Bath batch) {
        this.batch = batch;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
