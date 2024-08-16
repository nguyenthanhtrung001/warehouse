package com.example.productservice.dao.request;

public class ProductRequest {
    private String productName;
    private double weight;
    private String description;
    private Long productGroup;
    private Long brand;
    private String images;
    private Long prices;
    private Long employeeId;
    private  Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductRequest() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(Long productGroup) {
        this.productGroup = productGroup;
    }

    public Long getBrand() {
        return brand;
    }

    public void setBrand(Long brand) {
        this.brand = brand;
    }

    public Long getPrices() {
        return prices;
    }

    public void setPrices(Long prices) {
        this.prices = prices;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

}
