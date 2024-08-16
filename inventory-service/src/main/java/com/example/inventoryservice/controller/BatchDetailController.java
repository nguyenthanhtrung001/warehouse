package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.response.OrderQuantity;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.Batch;
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
    public ResponseEntity<String> updateQuantityForExport(@PathVariable Long id, @RequestParam Integer quantity) {
        boolean updated = batchDetailService.updateQuantityDeliveryDetail(id, quantity);
        if (updated) {
            return ResponseEntity.ok("Cập nhật thành công");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/quantity/product/{id}")
    public ResponseEntity<List<OrderQuantity>> updateQuantityForOrder(@PathVariable Long id, @RequestParam Integer quantity) {
        List<OrderQuantity> updated = batchDetailService.updateQuantityForOrder(id, quantity);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/update-quantity-return-order/{id}")
    public ResponseEntity<String> updateQuantityForReturnOrder(
            @PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity) {
        boolean isUpdated = batchDetailService.updateQuantityForReturnOrder(id, quantity);
        if (isUpdated) {
            return ResponseEntity.ok("Quantity updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batch detail not found.");
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
    public Integer getQuantityByIdProductId(@PathVariable Long productId) {
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
    @GetMapping("/idproduct/{id}")
    public String getProductByBatchDetailId(@PathVariable Long id) {
       System.out.println("Đã vào đây");
       return batchDetailService.getProductByBatchDetailById(id);
    }
    @GetMapping("/{id}/batch")
    public Batch getBatchByBatchDetailById(@PathVariable Long id) {
        return batchDetailService.getBatchByBatchDetailById(id);
    }
    @GetMapping("/product/{productId}")
    public List<BatchDetail> getBatchDetailsByProductId(@PathVariable Long productId) {
        return batchDetailService.getBatchDetailsByProductId(productId);
    }
    @GetMapping("/top-lowest-quantity")
    public List<ProductQuantity> getTopLowestQuantity(@RequestParam int limit) {
        return batchDetailService.getTopNLowestQuantity(limit);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteBatchDetailReturnBatchID(@RequestBody List<Long> listID) {
        boolean isDeleted = batchDetailService.deleteBatchDetailReturnBathID(listID);
        return ResponseEntity.ok(isDeleted);
    }
}
