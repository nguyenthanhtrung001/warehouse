package com.example.productservice.client;



import com.example.productservice.dao.response.BatchLocation;
import com.example.productservice.dao.response.ProductQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "inventory-service", url = "http://localhost:8086")
public interface InventoryClient {

   @GetMapping (value = "/api/batch-details/top-lowest-quantity", produces = MediaType.APPLICATION_JSON_VALUE)
   List<ProductQuantity> getTopNLowestQuantity(@RequestParam("limit") Integer limit);
   @GetMapping (value = "/api/batch-details/quantity/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
   Integer getQuantityByProductId(@PathVariable("productId") Long id);
   @GetMapping (value = "/api/batches/batch-location/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
   BatchLocation getBatchLocationForProductId(@PathVariable("productId") Long id);

   @GetMapping( value = "/api/batches/expired", produces = MediaType.APPLICATION_JSON_VALUE)
   List<Long> getExpiredProductIds();
}

