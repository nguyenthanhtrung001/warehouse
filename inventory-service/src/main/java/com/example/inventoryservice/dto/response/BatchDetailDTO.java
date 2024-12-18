package com.example.inventoryservice.dto.response;

import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.entity.Location;

public class BatchDetailDTO {

    private Long id;
    private ProductResponse product;
    private Integer quantity;
    private Location location;

    public BatchDetailDTO() {
    }

    public BatchDetailDTO(Long id, Integer quantity, Location location) {
        this.id = id;
        this.quantity = quantity;
        this.location = location;
    }

    public BatchDetailDTO(Long id, ProductResponse product, Integer quantity, Location location) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
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
