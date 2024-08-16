package com.example.orderservice.dto.response;

import java.util.List;

public class Order {
    Long productId;
    List<OrderQuantity> quantityList;

    public Order() {
    }

    public Order(Long productId, List<OrderQuantity> quantityList) {
        this.productId = productId;
        this.quantityList = quantityList;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<OrderQuantity> getQuantityList() {
        return quantityList;
    }

    public void setQuantityList(List<OrderQuantity> quantityList) {
        this.quantityList = quantityList;
    }
}
