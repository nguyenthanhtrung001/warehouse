package com.example.goodsservice.dto.response;

public class ReceiptSummary {

    private long totalReceipts;
    private long totalPurchasePrice;

    public ReceiptSummary(long totalReceipts, long totalPurchasePrice) {
        this.totalReceipts = totalReceipts;
        this.totalPurchasePrice = totalPurchasePrice;
    }

    // Getters and Setters
    public long getTotalReceipts() {
        return totalReceipts;
    }

    public void setTotalReceipts(long totalReceipts) {
        this.totalReceipts = totalReceipts;
    }

    public long getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public void setTotalPurchasePrice(long totalPurchasePrice) {
        this.totalPurchasePrice = totalPurchasePrice;
    }
}
