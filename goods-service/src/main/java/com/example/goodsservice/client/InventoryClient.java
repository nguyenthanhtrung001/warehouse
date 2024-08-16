package com.example.goodsservice.client;

import com.example.goodsservice.dto.BathDetailRequest;
import com.example.goodsservice.dto.BathRequest;
import com.example.goodsservice.dto.response.ProductQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "inventory-service", url = "http://localhost:8086")
public interface InventoryClient {

   @PostMapping(value = "/api/batches",produces = MediaType.APPLICATION_JSON_VALUE)
   BathRequest createBath(@RequestBody BathRequest bathRequest);

   @GetMapping( value = "/api/batch-details/{id}/batch", produces = MediaType.APPLICATION_JSON_VALUE)
   BathRequest getBathByDetail(@PathVariable("id") Long id);
   @PostMapping(value = "/api/batch-details",produces = MediaType.APPLICATION_JSON_VALUE)
   BathDetailRequest createDetailBath(@RequestBody BathDetailRequest Request);
   @PutMapping( value = "/api/batch-details/quantity/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   ResponseEntity<String> updateDetailBathForDelivery(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);
   @GetMapping( value = "/api/inventory-check-slips/products/discrepancies/by-month-year", produces = MediaType.APPLICATION_JSON_VALUE)
   List<ProductQuantity> getProductQuantity_import_check_inventory(@RequestParam("month") Integer month, @RequestParam("year") Integer year);
   @GetMapping( value = "/api/inventory-check-slips/products/discrepancies-less/by-month-year", produces = MediaType.APPLICATION_JSON_VALUE)
   List<ProductQuantity> getProductQuantity_export_check_inventory(@RequestParam("month") Integer month, @RequestParam("year") Integer year);

   @DeleteMapping(value = "/api/batch-details/delete",produces = MediaType.APPLICATION_JSON_VALUE)
   boolean deleteBatchDetailReturnBatchID(@RequestBody List<Long> listID);

   @PutMapping( value = "/api/batch-details/update-quantity-return-order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   String updateQuantityForDeleteDelivery(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);




}

