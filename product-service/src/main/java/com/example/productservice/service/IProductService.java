package com.example.productservice.service;

import com.example.productservice.entity.Product;

import java.util.List;

public interface IProductService {
    public List<Product> getAllProducts();
    public Product getProductById(Long id);
    public Product createProduct(Product product);
    public boolean updateProduct(Product product, Long id);
    public boolean deleteProduct(Long id);

}
