package com.example.productservice.controller;

import com.example.productservice.entity.Brand;
import com.example.productservice.entity.Product;
import com.example.productservice.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity addProduct(@RequestBody Product product) {

        productService.createProduct(product);
        return new ResponseEntity<>("Product created successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
       boolean isDelete= productService.deleteProduct(id);
       if(isDelete)
       {
           return new ResponseEntity<>("Product Deleted successfully", HttpStatus.OK);
       } return new ResponseEntity<>("Product Deleted fail", HttpStatus.NOT_FOUND);


    }

    @PutMapping("/{id}")
    public ResponseEntity updateBrand(@PathVariable Long id, @RequestBody Product product) {
        productService.updateProduct(product,id);
        return new ResponseEntity<>("Product update successfully", HttpStatus.OK);

    }
}
