package com.example.productservice.client;

import com.example.productservice.dao.response.ProductQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:8087")

public interface InvoiceClient {
    @GetMapping (value = "/api/invoice-details/top-product", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductQuantity> getTopProductSale(@RequestParam("top") Integer limit);
    @GetMapping (value = "/api/invoice-details/quantities/last-three-months", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductQuantity getQuantityProductSaleThreeMonth(@RequestParam("productId") Long productId);

}
