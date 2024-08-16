package com.example.inventoryservice.dto;

public class ProductResponse {

    private Long id;
    private String productName;
    private double weight;
    private String description;
    private int status;

    public ProductResponse() {
    }


    public Long getId() {
        return this.id;
    }

    public String getProductName() {
        return this.productName;
    }

    public double getWeight() {
        return this.weight;
    }

    public String getDescription() {
        return this.description;
    }

    public int getStatus() {
        return this.status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProductResponse)) return false;
        final ProductResponse other = (ProductResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$productName = this.getProductName();
        final Object other$productName = other.getProductName();
        if (this$productName == null ? other$productName != null : !this$productName.equals(other$productName))
            return false;
        if (Double.compare(this.getWeight(), other.getWeight()) != 0) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        if (this.getStatus() != other.getStatus()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ProductResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $productName = this.getProductName();
        result = result * PRIME + ($productName == null ? 43 : $productName.hashCode());
        final long $weight = Double.doubleToLongBits(this.getWeight());
        result = result * PRIME + (int) ($weight >>> 32 ^ $weight);
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        result = result * PRIME + this.getStatus();
        return result;
    }

    public String toString() {
        return "ProductResponse(id=" + this.getId() + ", productName=" + this.getProductName() + ", weight=" + this.getWeight() + ", description=" + this.getDescription() + ", status=" + this.getStatus() + ")";
    }
}