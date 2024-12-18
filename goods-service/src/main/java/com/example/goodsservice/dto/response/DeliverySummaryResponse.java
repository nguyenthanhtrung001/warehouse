package com.example.goodsservice.dto.response;

public class DeliverySummaryResponse {

    private Long totalDeliveryNotes; // Tổng số phiếu DeliveryNote
    private Long totalQuantity;     // Tổng số lượng

    public DeliverySummaryResponse(Long totalDeliveryNotes, Long totalQuantity) {
        this.totalDeliveryNotes = totalDeliveryNotes;
        this.totalQuantity = totalQuantity;
    }

    // Getters and Setters
    public Long getTotalDeliveryNotes() {
        return totalDeliveryNotes;
    }

    public void setTotalDeliveryNotes(Long totalDeliveryNotes) {
        this.totalDeliveryNotes = totalDeliveryNotes;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
