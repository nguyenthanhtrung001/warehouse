package com.example.mlservice.client;
import com.example.mlservice.dto.request.Import_Export_Request;
import com.example.mlservice.dto.request.InventoryCheckSlipRequest;
import com.example.mlservice.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "inventory-service", url = "http://localhost:8086")
public interface InventoryClient {
   // lấy chi tiết trong lô hàng
   @GetMapping( value = "/api/batch-details/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
   List<BatchDetail> getBatchDetailsByProductId(@PathVariable Long productId, @RequestParam Long warehouseId);
   // lấy ds vị trí trong kho hàng
   @GetMapping( value = "/api/locations/warehouse/{warehouseId}", produces = MediaType.APPLICATION_JSON_VALUE)
   ResponseEntity<List<Location>> getAllLocationsInWarehouse(@PathVariable Long warehouseId);
   // tạo phiếu kiểm kho
   @PostMapping(value = "/api/inventory-check-slips", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   ResponseEntity<InventoryCheckSlip> createInventoryCheckSlip(@RequestBody InventoryCheckSlipRequest inventoryCheckSlipRequest);
   // lấy danh sách lô hàng
   @GetMapping(value = "/api/batches/batches/details", produces = MediaType.APPLICATION_JSON_VALUE)
   List<BatchDetailInfo> getBatchDetailsByWarehouseId(@RequestParam Long warehouseId);
  // lấy chi tiết thông tin lô hàng
   @GetMapping(value = "/api/batch-details/batch/{batchId}", produces = MediaType.APPLICATION_JSON_VALUE)
   List<BatchDetailDTO> getBatchDetailsByBatchId(@PathVariable Long batchId);
   // lấy chi tiết thông tin tại vị trí trong kho
   @GetMapping(value = "/api/batch-details/location/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
   ResponseEntity<List<ProductLocation>> getBatchDetailsByLocationId(@PathVariable Long locationId);

}

