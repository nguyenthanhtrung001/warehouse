package com.example.productservice.dao.request;

public class PriceRequest {
    private Long productId;
    private Long price;
    private Long employeeId;

    public PriceRequest() {
    }

    public PriceRequest(Long product, Long price, Long employeeId) {
        this.productId = product;
        this.price = price;
        this.employeeId = employeeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
