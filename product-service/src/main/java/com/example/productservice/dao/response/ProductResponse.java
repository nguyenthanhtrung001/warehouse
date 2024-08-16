package com.example.productservice.dao.response;

import com.example.productservice.entity.Brand;
import com.example.productservice.entity.ProductGroup;

import java.util.List;

public class ProductResponse {
    private Long id;

    private String productName;

    private double weight;

    private String description;

    private ProductGroup productGroup;

    private Brand brand;

    private String image;
    private Long price;
    private Integer quantity;

    private BatchLocation batchLocation;

    public BatchLocation getBatchLocation() {
        return batchLocation;
    }

    public void setBatchLocation(BatchLocation batchLocation) {
        this.batchLocation = batchLocation;
    }

    public ProductResponse() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public ProductGroup getProductGroup() {
        return this.productGroup;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return this.price;
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

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }



    public void setPrice(Long price) {
        this.price = price;
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
        final Object this$productGroup = this.getProductGroup();
        final Object other$productGroup = other.getProductGroup();
        if (this$productGroup == null ? other$productGroup != null : !this$productGroup.equals(other$productGroup))
            return false;
        final Object this$brand = this.getBrand();
        final Object other$brand = other.getBrand();
        if (this$brand == null ? other$brand != null : !this$brand.equals(other$brand)) return false;
        final Object this$image = this.getImage();
        final Object other$image = other.getImage();
        if (this$image == null ? other$image != null : !this$image.equals(other$image)) return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
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
        final Object $productGroup = this.getProductGroup();
        result = result * PRIME + ($productGroup == null ? 43 : $productGroup.hashCode());
        final Object $brand = this.getBrand();
        result = result * PRIME + ($brand == null ? 43 : $brand.hashCode());
        final Object $image = this.getImage();
        result = result * PRIME + ($image == null ? 43 : $image.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        return result;
    }

    public String toString() {
        return "ProductResponse(id=" + this.getId() + ", productName=" + this.getProductName() + ", weight=" + this.getWeight() + ", description=" + this.getDescription() + ", productGroup=" + this.getProductGroup() + ", brand=" + this.getBrand() + ", image=" + this.getImage() + ", price=" + this.getPrice() + ")";
    }
}
