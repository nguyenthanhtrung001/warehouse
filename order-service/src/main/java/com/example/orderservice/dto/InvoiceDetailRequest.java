package com.example.orderservice.dto;

public class InvoiceDetailRequest {
    private Long product_Id;
    private Long purchasePrice;
    private Integer quantity;

    public InvoiceDetailRequest() {
    }

    public InvoiceDetailRequest(Long product_Id, Long purchasePrice, Integer quantity) {
        this.product_Id = product_Id;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
    }

    public Long getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(Long product_Id) {
        this.product_Id = product_Id;
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
