package com.example.goodsservice.dto;

public class Import_Export_DetailRequest {
    private Long product_Id;
    private Double purchasePrice;
    private Integer quantity;
    private Long batchDetail_Id;

    public Long getBatchDetail_Id() {
        return batchDetail_Id;
    }

    public void setBatchDetail_Id(Long batchDetail_Id) {
        this.batchDetail_Id = batchDetail_Id;
    }

    public Import_Export_DetailRequest() {
    }

    public Long getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(Long product_Id) {
        this.product_Id = product_Id;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
