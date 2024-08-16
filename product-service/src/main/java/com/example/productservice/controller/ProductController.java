package com.example.productservice.controller;

import com.example.productservice.dao.request.ProductRequest;
import com.example.productservice.dao.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
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
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/has-batch-location")
    public List<ProductResponse> getAllProductsHasLocationBatch() {
        return productService.getAllProductsHasLocationBatch();
    }
    @GetMapping("/count")
    public int getProductCount() {
        return productService.getProductCount();
    }
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @GetMapping("/nameproduct/{id}")
    public String getNameProductById(@PathVariable Long id) {
        return productService.getNameProductById(id);
    }

    @PostMapping
    public ResponseEntity addProduct(@RequestBody ProductRequest product) {

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

    @PutMapping()
    public ResponseEntity updateProduct(@RequestBody ProductRequest product) {
        productService.updateProduct(product);
        return new ResponseEntity<>("Product update successfully", HttpStatus.OK);

    }
    @GetMapping("/top-lowest")
    public List<ProductResponse> getTopLowestProduct(@RequestParam("top") Integer top) {
        return productService.getTopLowestProduct(top);
    }
    @GetMapping("/top-sale")
    public List<ProductResponse> getTopProductSale(@RequestParam("top") Integer top) {
        return productService.getTopProductSale(top);
    }
    @GetMapping("/propose")
    public List<ProductResponse> getProposeProduct() {
        return productService.getProposeProduct();
    }
    @GetMapping("/expired")
    public List<ProductResponse> getExpiredProduct() {
        return productService.getExpiredProduct();
    }
    @GetMapping("/notify-lowest")
    public ResponseEntity<List<ProductResponse>> getNotifyTopLowestProduct() {
        List<ProductResponse> products = productService.getNotifyTopLowestProduct(10);
        return ResponseEntity.ok(products);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateProductStatus(@PathVariable("id") Long productId) {
        try {
            productService.DeleteProductWithStatus(productId);
            return ResponseEntity.ok("Cập nhật trạng thái sản phẩm thành công.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Không tìm thấy sản phẩm với ID: " + productId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã xảy ra lỗi khi cập nhật trạng thái sản phẩm.");
        }
    }
}
