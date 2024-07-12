package com.example.goodsservice.client;

import com.example.goodsservice.dto.BathDetailRequest;
import com.example.goodsservice.dto.BathRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "inventory-service", url = "http://localhost:8086")
public interface InventoryClient {
   @PostMapping(value = "/api/batches",produces = MediaType.APPLICATION_JSON_VALUE)
   BathRequest createBath(@RequestBody BathRequest bathRequest);

   @PostMapping(value = "/api/batch-details",produces = MediaType.APPLICATION_JSON_VALUE)
   BathDetailRequest createDetailBath(@RequestBody BathDetailRequest Request);
   @PutMapping( value = "/api/batch-details/quantity/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   ResponseEntity<String> updateDetailBath(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);
}

