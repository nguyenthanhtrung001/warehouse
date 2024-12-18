package com.example.mlservice.dto;
public class ForecastItemSummary {
    private String itemId;
    private String productName;
    private Long forecastValue;
    private Long inventory;

    public ForecastItemSummary(String itemId, String productName, Long forecastValue, Long inventory) {
        this.itemId = itemId;
        this.productName = productName;
        this.forecastValue = forecastValue;
        this.inventory = inventory;
    }

    // Getters và Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Long getInventory() {
        return inventory;
    }

    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getForecastValue() {
        return forecastValue;
    }

    public void setForecastValue(Long forecastValue) {
        this.forecastValue = forecastValue;
    }
}
