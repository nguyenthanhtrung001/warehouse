package com.example.goodsservice.client;

import com.example.goodsservice.dto.response.ProductQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:8087")
public interface OrderClient {
    @GetMapping( value = "/api/invoice-details/products/quantities/by-month-year", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductQuantity> getProductQuantity_export_return_order(@RequestParam("month") Integer month, @RequestParam("year") Integer year);

    @GetMapping( value = "/api/return-details/products/quantities/by-month-year", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductQuantity> getProductQuantity_import_order(@RequestParam("month") Integer month, @RequestParam("year") Integer year);

}
