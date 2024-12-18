package com.example.orderservice.dto.response;

public class MonthRevenue {
    String month;
    Long revenue;

    public MonthRevenue(String month, Long revenue) {
        this.month = month;
        this.revenue = revenue;
    }

    public MonthRevenue() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }
}
