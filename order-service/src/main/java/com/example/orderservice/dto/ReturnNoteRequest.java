package com.example.orderservice.dto;

import java.util.List;

public class ReturnNoteRequest {
    private Long invoiceId;
    private Long employeeId;
    private Integer status;
    private Long price;
    private List<ReturnDetailRequest> returnDetails;

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    // Getters and setters
    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ReturnDetailRequest> getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(List<ReturnDetailRequest> returnDetails) {
        this.returnDetails = returnDetails;
    }
}
