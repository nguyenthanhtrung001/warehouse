package com.example.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods-service", url = "http://localhost:8088")

public interface ReceiptClient {
    @GetMapping(value = "/api/receipt-details/exists/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean checkProductIdExists(@PathVariable Long productId);

}
