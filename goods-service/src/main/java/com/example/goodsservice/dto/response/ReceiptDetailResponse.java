package com.example.goodsservice.dto.response;

import com.example.goodsservice.dto.Bath;
import com.example.goodsservice.dto.BathRequest;
import com.example.goodsservice.dto.Location;
import com.example.goodsservice.entity.Receipt;

public class ReceiptDetailResponse {
    private Long id;
    private Receipt receipt;
    private Long batchDetail_Id;
    private Double purchasePrice;
    private Integer quantity;
    private String nameProduct;

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }

    private Long ProductId;
    private BathRequest bath;
    private Location location;

    public BathRequest getBath() {
        return bath;
    }

    public void setBath(BathRequest bath) {
        this.bath = bath;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ReceiptDetailResponse() {
    }

    public Long getId() {
        return this.id;
    }

    public Receipt getReceipt() {
        return this.receipt;
    }

    public Long getBatchDetail_Id() {
        return this.batchDetail_Id;
    }

    public Double getPurchasePrice() {
        return this.purchasePrice;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public String getNameProduct() {
        return this.nameProduct;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public void setBatchDetail_Id(Long batchDetail_Id) {
        this.batchDetail_Id = batchDetail_Id;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReceiptDetailResponse)) return false;
        final ReceiptDetailResponse other = (ReceiptDetailResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$receipt = this.getReceipt();
        final Object other$receipt = other.getReceipt();
        if (this$receipt == null ? other$receipt != null : !this$receipt.equals(other$receipt)) return false;
        final Object this$batchDetail_Id = this.getBatchDetail_Id();
        final Object other$batchDetail_Id = other.getBatchDetail_Id();
        if (this$batchDetail_Id == null ? other$batchDetail_Id != null : !this$batchDetail_Id.equals(other$batchDetail_Id))
            return false;
        final Object this$purchasePrice = this.getPurchasePrice();
        final Object other$purchasePrice = other.getPurchasePrice();
        if (this$purchasePrice == null ? other$purchasePrice != null : !this$purchasePrice.equals(other$purchasePrice))
            return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        final Object this$nameProduct = this.getNameProduct();
        final Object other$nameProduct = other.getNameProduct();
        if (this$nameProduct == null ? other$nameProduct != null : !this$nameProduct.equals(other$nameProduct))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReceiptDetailResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $receipt = this.getReceipt();
        result = result * PRIME + ($receipt == null ? 43 : $receipt.hashCode());
        final Object $batchDetail_Id = this.getBatchDetail_Id();
        result = result * PRIME + ($batchDetail_Id == null ? 43 : $batchDetail_Id.hashCode());
        final Object $purchasePrice = this.getPurchasePrice();
        result = result * PRIME + ($purchasePrice == null ? 43 : $purchasePrice.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        final Object $nameProduct = this.getNameProduct();
        result = result * PRIME + ($nameProduct == null ? 43 : $nameProduct.hashCode());
        return result;
    }

    public String toString() {
        return "ReceiptDetailResponse(id=" + this.getId() + ", receipt=" + this.getReceipt() + ", batchDetail_Id=" + this.getBatchDetail_Id() + ", purchasePrice=" + this.getPurchasePrice() + ", quantity=" + this.getQuantity() + ", nameProduct=" + this.getNameProduct() + ")";
    }
}
