package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.response.BatchLocation;
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
    @GetMapping("/batch-location/{productId}")
    public BatchLocation getBatchLocationForProduct(@PathVariable Long productId) {
        return batchService.getBatchLocatonForProduct(productId);
    }
    // Controller để lấy danh sách productId đã hết hạn
    @GetMapping("/expired")
    public List<Long> getExpiredProductIds() {
        return batchService.getExpiredProductIds();
    }

    // Controller để lấy danh sách productId sắp hết hạn trong 7 ngày
    @GetMapping("/expiring-in-7-days")
    public List<Long> getExpiringProductIdsIn7Days() {
        return batchService.getProductIdsWithBatchesExpiringIn7Days();
    }
}