package com.example.orderservice.dto.response;

public class OrderQuantity {
    Long bathDetail_Id;
    Integer quantity;

    public OrderQuantity() {
    }

    public Long getBathDetail_Id() {
        return bathDetail_Id;
    }

    public void setBathDetail_Id(Long bathDetail_Id) {
        this.bathDetail_Id = bathDetail_Id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
