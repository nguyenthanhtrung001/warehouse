package com.example.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brandName;

    private String description;  // Thêm trường mô tả

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    private List<Product> products;

    // Constructor không tham số
    public Brand() {}

    // Constructor có tham số
    public Brand(Long id) {
        this.id = id;
    }

    // Getter và Setter cho trường products
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Getter và Setter cho trường id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter và Setter cho trường brandName
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    // Getter và Setter cho trường description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
