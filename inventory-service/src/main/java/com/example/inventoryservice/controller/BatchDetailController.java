package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.service.IBatchDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch-details")
public class BatchDetailController {

    @Autowired
    private  IBatchDetailService batchDetailService;



    @PostMapping
    public ResponseEntity<BatchDetail> createBatchDetail(@RequestBody BatchDetail batchDetail) {
        BatchDetail createdBatchDetail = batchDetailService.createBatchDetail(batchDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBatchDetail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchDetail> getBatchDetailById(@PathVariable Long id) {
        BatchDetail batchDetail = batchDetailService.getBatchDetailById(id);
        return ResponseEntity.ok(batchDetail);
    }

    @GetMapping
    public ResponseEntity<List<BatchDetail>> getAllBatchDetails() {
        List<BatchDetail> batchDetails = batchDetailService.getAllBatchDetails();
        return ResponseEntity.ok(batchDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchDetail> updateBatchDetail(@PathVariable Long id, @RequestBody BatchDetail batchDetail) {
        boolean updated = batchDetailService.updateBatchDetail(id, batchDetail);
        if (updated) {
            return ResponseEntity.ok(batchDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/quantity/{id}")
    public ResponseEntity<String> updateQuantityBatchDetail(@PathVariable Long id, @RequestParam Integer quantity) {
        boolean updated = batchDetailService.updateQuantityDeliveryDetail(id, quantity);
        if (updated) {
            return ResponseEntity.ok("Cập nhật thành công");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatchDetail(@PathVariable Long id) {
        boolean deleted = batchDetailService.deleteBatchDetail(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/quantity/{productId}")
    public Long getQuantityByIdProductId(@PathVariable Long productId) {
        return batchDetailService.getQuantityByIdProductId(productId);
    }
    @GetMapping("/locations/{productId}")
    public List<Location> getLocationByProductId(@PathVariable Long productId) {
        return batchDetailService.getLocationByProductId(productId);
    }
    @GetMapping("/quantity")
    public ResponseEntity<Long> getQuantityByProductIdAndBatchId(
            @RequestParam Long productId,
            @RequestParam Long batchId
    ) {
        Long quantity = batchDetailService.getQuantityByProductIdAndBatchId(productId, batchId);
        return ResponseEntity.ok(quantity);
    }

    @GetMapping("/products/{batchId}")
    public List<Long> getProductByBatchId(@PathVariable Long batchId) {
        return batchDetailService.getProductByBatchId(batchId);
    }
}
