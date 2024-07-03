package com.example.productservice.controller;

import com.example.productservice.entity.Price;
import com.example.productservice.entity.Product;
import com.example.productservice.service.IPriceService;
import com.example.productservice.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPrice(@RequestBody Price price, @RequestParam Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductById(productId);
            price.setProduct(product);
            Price createdPrice = priceService.createPrice(price);

            response.put("result", 1);
            response.put("msg", "Thêm giá thành công");
            response.put("method", "POST");
            response.put("price", createdPrice);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            response.put("result", 0);
            response.put("msg", "Không thể thêm giá");
            response.put("method", "POST");
            response.put("error", ex.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePrice(@PathVariable Long id, @RequestBody Price price, @RequestParam Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductById(productId);
            price.setProduct(product);
            boolean updatedPrice = priceService.updatePrice(id, price);

            if (updatedPrice != false) {
                response.put("result", 1);
                response.put("msg", "Cập nhật giá thành công");
                response.put("method", "PUT");
                response.put("price", updatedPrice);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("result", 0);
                response.put("msg", "Không tìm thấy giá cần cập nhật");
                response.put("method", "PUT");

                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            response.put("result", 0);
            response.put("msg", "Không thể cập nhật giá");
            response.put("method", "PUT");
            response.put("error", ex.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePrice(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (priceService.deletePrice(id)) {
            response.put("result", 1);
            response.put("msg", "Xóa giá thành công");
            response.put("method", "DELETE");

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            response.put("result", 0);
            response.put("msg", "Không tìm thấy giá cần xóa");
            response.put("method", "DELETE");

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Price> getPriceById(@PathVariable Long id) {
        Price price = priceService.getPriceById(id);
        if (price != null) {
            return new ResponseEntity<>(price, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Price>> getAllPrices() {
        List<Price> prices = priceService.getAllPrices();
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }
}
