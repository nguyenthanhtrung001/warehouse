package com.example.mlservice.client;
import com.example.mlservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "product-service", url = "http://localhost:8084")
public interface ProductClient {

    @GetMapping (value = "/api/products/has-batch-location-warehouse/{warehouse}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResponse> getAllProductsHasLocationBatch(@PathVariable("warehouse") Long warehouse);
    @GetMapping(value = "/api/products/nameproduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    String getNameProductByID(@PathVariable("id") Long id);
    @GetMapping (value = "/api/products/has-quantity-by/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getProductByID(@PathVariable("id") Long id,  @RequestParam Long warehouseId);
}
