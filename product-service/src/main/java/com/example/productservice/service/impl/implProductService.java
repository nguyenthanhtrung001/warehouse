package com.example.productservice.service.impl;

import com.example.productservice.entity.Brand;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implProductService implements IProductService {

    private ProductRepository productRepository;

    public implProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateProduct(Product product, Long id) {
        Optional<Product> productOption= productRepository.findById(id);
        if(productOption.isPresent()){
            Product productUpdate= productOption.get();
            productUpdate.setProductName(product.getProductName());
            productUpdate.setDescription(product.getDescription());
            productUpdate.setStatus(product.getStatus());
            productUpdate.setWeight(product.getWeight());

            productUpdate.setBrand(product.getBrand());
            productUpdate.setProductGroup(product.getProductGroup());

            productRepository.save(productUpdate);
            return true;
        }else return false;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
