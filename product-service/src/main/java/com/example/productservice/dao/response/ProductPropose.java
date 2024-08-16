package com.example.productservice.dao.response;

public class ProductPropose {
    private Long productId;
    private Long quantitySale;
    private Long quantityInventory;

    public ProductPropose() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantitySale() {
        return quantitySale;
    }

    public void setQuantitySale(Long quantitySale) {
        this.quantitySale = quantitySale;
    }

    public Long getQuantityInventory() {
        return quantityInventory;
    }

    public void setQuantityInventory(Long quantityInventory) {
        this.quantityInventory = quantityInventory;
    }
}
