package com.example.orderservice.client;


import com.example.orderservice.dto.response.OrderQuantity;
import com.example.orderservice.dto.response.ProductQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "inventory-service", url = "http://localhost:8086")
public interface InventoryClient {

   @PutMapping( value = "/api/batch-details/quantity/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   List<OrderQuantity> updateDetailBathWithProduct(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);


   @PutMapping( value = "/api/batch-details/update-quantity-return-order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   String updateQuantityForReturnOrder(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);


}

