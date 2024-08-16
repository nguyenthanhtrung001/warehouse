package com.example.inventoryservice.client;

import com.example.inventoryservice.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.OpenOption;
import java.util.Optional;


@FeignClient(name = "product-service", url = "http://localhost:8084")
public interface ProductClient {
    @GetMapping (value = "/api/products/nameproduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    String getNameProductByID(@PathVariable("id") Long id);
}
