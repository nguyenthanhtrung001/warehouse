package com.example.orderservice.dto.response;


import java.time.LocalDateTime;

public class SalesData {

    private LocalDateTime printDate;
    private Long productId;
    private Long totalDemand;

    public SalesData(LocalDateTime printDate, Long productId, Long totalDemand) {
        this.printDate = printDate;
        this.productId = productId;
        this.totalDemand = totalDemand;
    }

    // Getters và Setters
    public LocalDateTime getPrintDate() {
        return printDate;
    }

    public void setPrintDate(LocalDateTime printDate) {
        this.printDate = printDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(Long totalDemand) {
        this.totalDemand = totalDemand;
    }
}
