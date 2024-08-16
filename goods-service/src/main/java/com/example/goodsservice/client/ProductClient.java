package com.example.goodsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8084")
public interface ProductClient {
    @GetMapping(value = "/api/products/nameproduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    String getNameProductByID(@PathVariable("id") Long id);
}
