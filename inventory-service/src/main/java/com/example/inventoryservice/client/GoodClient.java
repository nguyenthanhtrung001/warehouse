package com.example.inventoryservice.client;

import com.example.inventoryservice.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods-service", url = "http://localhost:8088")
public interface GoodClient {
    @GetMapping(value = "/api/warehouses/capacity/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Double getQuantityWarehouse(@PathVariable("id") Long id);
}
