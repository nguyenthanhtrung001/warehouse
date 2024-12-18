package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.dto.response.BatchDetailInfo;
import com.example.inventoryservice.dto.response.BatchLocation;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.service.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BatchController {
    @Autowired
    private  IBatchService batchService;


    @PostMapping
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {
        Batch createdBatch = batchService.createBatch(batch);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBatch);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getBatchById(@PathVariable Long id) {
        Batch batch = batchService.getBatchById(id);
        return ResponseEntity.ok(batch);
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches() {
        List<Batch> batches = batchService.getAllBatches();
        return ResponseEntity.ok(batches);
    }
    @GetMapping("/in-warehouse/{warehouseId}")
    public List<Batch> getBatchesByWarehouseId(@PathVariable Long warehouseId) {
        return batchService.getAllBatchesForWarehouseId(warehouseId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Batch> updateBatch(@PathVariable Long id, @RequestBody Batch batch) {
        boolean updated = batchService.updateBatch(id, batch);
        if (updated) {
            return ResponseEntity.ok(batch);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        boolean deleted = batchService.deleteBatch(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/batch-location/{productId}/{warehouseId}")
    public BatchLocation getBatchLocationForProduct(
            @PathVariable Long productId,
            @PathVariable Long warehouseId) {
        return batchService.getBatchLocatonForProduct(productId, warehouseId);
    }

    // Controller để lấy danh sách productId đã hết hạn
    @GetMapping("/expired")
    public List<Long> getExpiredProductIds(@RequestParam Long warehouseId) {
        return batchService.getExpiredProductIds(warehouseId);
    }

    // Controller để lấy danh sách productId sắp hết hạn trong 7 ngày
    @GetMapping("/expiring-in-7-days")
    public List<Long> getExpiringProductIdsIn7Days() {
        return batchService.getProductIdsWithBatchesExpiringIn7Days();
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ProductResponse>> getProductsByWarehouseId(@PathVariable Long warehouseId) {
        try {
            List<ProductResponse> products = batchService.getProductsByWarehouseId(warehouseId);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            // Xử lý lỗi (có thể log lại hoặc gửi thông báo lỗi phù hợp)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/batches/details")
    public List<BatchDetailInfo> getBatchDetailsByWarehouseId(@RequestParam Long warehouseId) {
        return batchService.getBatchDetailsByWarehouseId(warehouseId);
    }
}