package com.example.goodsservice.dto.response;

import com.example.goodsservice.dto.BathRequest;
import com.example.goodsservice.dto.Location;
import com.example.goodsservice.entity.DeliveryNote;

public class NoteDetailResponse {
    private Long id;
    private DeliveryNote deliveryNote;
    private Long batchDetail_Id;
    private Double purchasePrice;
    private Integer quantity;
    private String nameProduct;
    private Long ProductId;
    private BathRequest bath;
    private Location location;

    public NoteDetailResponse() {
    }


    public Long getId() {
        return this.id;
    }

    public DeliveryNote getDeliveryNote() {
        return this.deliveryNote;
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

    public Long getProductId() {
        return this.ProductId;
    }

    public BathRequest getBath() {
        return this.bath;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
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

    public void setProductId(Long ProductId) {
        this.ProductId = ProductId;
    }

    public void setBath(BathRequest bath) {
        this.bath = bath;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NoteDetailResponse)) return false;
        final NoteDetailResponse other = (NoteDetailResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$deliveryNote = this.getDeliveryNote();
        final Object other$deliveryNote = other.getDeliveryNote();
        if (this$deliveryNote == null ? other$deliveryNote != null : !this$deliveryNote.equals(other$deliveryNote))
            return false;
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
        final Object this$ProductId = this.getProductId();
        final Object other$ProductId = other.getProductId();
        if (this$ProductId == null ? other$ProductId != null : !this$ProductId.equals(other$ProductId)) return false;
        final Object this$bath = this.getBath();
        final Object other$bath = other.getBath();
        if (this$bath == null ? other$bath != null : !this$bath.equals(other$bath)) return false;
        final Object this$location = this.getLocation();
        final Object other$location = other.getLocation();
        if (this$location == null ? other$location != null : !this$location.equals(other$location)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NoteDetailResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $deliveryNote = this.getDeliveryNote();
        result = result * PRIME + ($deliveryNote == null ? 43 : $deliveryNote.hashCode());
        final Object $batchDetail_Id = this.getBatchDetail_Id();
        result = result * PRIME + ($batchDetail_Id == null ? 43 : $batchDetail_Id.hashCode());
        final Object $purchasePrice = this.getPurchasePrice();
        result = result * PRIME + ($purchasePrice == null ? 43 : $purchasePrice.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        final Object $nameProduct = this.getNameProduct();
        result = result * PRIME + ($nameProduct == null ? 43 : $nameProduct.hashCode());
        final Object $ProductId = this.getProductId();
        result = result * PRIME + ($ProductId == null ? 43 : $ProductId.hashCode());
        final Object $bath = this.getBath();
        result = result * PRIME + ($bath == null ? 43 : $bath.hashCode());
        final Object $location = this.getLocation();
        result = result * PRIME + ($location == null ? 43 : $location.hashCode());
        return result;
    }

    public String toString() {
        return "NoteDetailResponse(id=" + this.getId() + ", deliveryNote=" + this.getDeliveryNote() + ", batchDetail_Id=" + this.getBatchDetail_Id() + ", purchasePrice=" + this.getPurchasePrice() + ", quantity=" + this.getQuantity() + ", nameProduct=" + this.getNameProduct() + ", ProductId=" + this.getProductId() + ", bath=" + this.getBath() + ", location=" + this.getLocation() + ")";
    }
}