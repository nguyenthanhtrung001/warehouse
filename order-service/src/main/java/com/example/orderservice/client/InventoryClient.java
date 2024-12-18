package com.example.orderservice.client;


import com.example.orderservice.dto.response.OrderQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "inventory-service", url = "http://localhost:8086")
public interface InventoryClient {

   @PutMapping( value = "/api/batch-details/quantity/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   List<OrderQuantity> updateDetailBathWithProduct(@PathVariable Long id, @RequestParam Integer quantity,  @RequestParam Long warehouseId);

   @GetMapping (value = "/api/batch-details/lock/quantity/{productId}/{warehouseId}", produces = MediaType.APPLICATION_JSON_VALUE)
   Integer getQuantityByProductIdAndWarehouse_lock(@PathVariable("productId") Long id, @PathVariable("warehouseId") Long warehouseId );

   @PutMapping( value = "/api/batch-details/update-quantity-return-order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   String updateQuantityForReturnOrder(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);


}

