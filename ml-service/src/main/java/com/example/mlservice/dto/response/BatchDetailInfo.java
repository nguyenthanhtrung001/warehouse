package com.example.mlservice.dto.response;

import java.util.Date;

public class BatchDetailInfo {

    private String batchName;
    private Long productId;
    private String productName;
    private Long totalQuantity;
    private String location;
    private Date expiryDate;

    private Long ProductBatchDetailId; // Chỉ một BatchDetail ID

    public BatchDetailInfo(String batchName, Long productId, Long totalQuantity, String location, Date expiryDate, Long batchDetailId) {
        this.batchName = batchName;
        this.productId = productId;
        this.totalQuantity = totalQuantity;
        this.location = location;
        this.expiryDate = expiryDate;
        this.ProductBatchDetailId = batchDetailId;
    }

    // Getters and Setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getProductbatchDetailId() {
        return ProductBatchDetailId;
    }

    public void setProductbatchDetailId(Long productbatchDetailId) {
        this.ProductBatchDetailId = productbatchDetailId;
    }
}
