package com.example.goodsservice.client;

import com.example.goodsservice.dto.BathDetailRequest;
import com.example.goodsservice.dto.BathRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "inventory-service", url = "http://localhost:8086")
public interface InventoryClient {
   @PostMapping(value = "/api/batches",produces = MediaType.APPLICATION_JSON_VALUE)
   BathRequest createBath(@RequestBody BathRequest bathRequest);

   @PostMapping(value = "/api/batch-details",produces = MediaType.APPLICATION_JSON_VALUE)
   BathDetailRequest createDetailBath(@RequestBody BathDetailRequest Request);
}

