package com.example.goodsservice.dto.response;

public class ProductSummary {

    private Long idProduct;
    private String productName;
    private Integer quantityDeliveryDetail;
    private Integer quantityReceiptDetail;
    private Long purchasePrice;
    private Long price;

    public ProductSummary(Long idProduct) {
        this.idProduct = idProduct;
        this.quantityDeliveryDetail = 0;
        this.quantityReceiptDetail = 0;
        this.purchasePrice = 0L;
        this.price = 0L;
        this.productName = "";
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getQuantityDeliveryDetail() {
        return quantityDeliveryDetail;
    }

    public void setQuantityDeliveryDetail(Integer quantityDeliveryDetail) {
        this.quantityDeliveryDetail = quantityDeliveryDetail;
    }

    public Integer getQuantityReceiptDetail() {
        return quantityReceiptDetail;
    }

    public void setQuantityReceiptDetail(Integer quantityReceiptDetail) {
        this.quantityReceiptDetail = quantityReceiptDetail;
    }

    public Long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductSummary{" +
                "idProduct=" + idProduct +
                ", quantityDeliveryDetail=" + quantityDeliveryDetail +
                ", quantityReceiptDetail=" + quantityReceiptDetail +
                ", purchasePrice=" + purchasePrice +
                ", price=" + price +
                '}';
    }
}
