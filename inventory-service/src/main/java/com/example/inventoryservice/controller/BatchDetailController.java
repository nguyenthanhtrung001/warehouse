package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.response.BatchDetailDTO;
import com.example.inventoryservice.dto.response.OrderQuantity;
import com.example.inventoryservice.dto.response.ProductLocation;
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

//    @PutMapping("/{id}")
//    public ResponseEntity<BatchDetail> updateBatchDetail(@PathVariable Long id, @RequestBody BatchDetail batchDetail) {
//        boolean updated = batchDetailService.updateBatchDetail(id, batchDetail);
//        if (updated) {
//            return ResponseEntity.ok(batchDetail);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
    @PutMapping("/{id}")
    public ResponseEntity<BatchDetail> updateLocationForProductWithBatchDetail(@PathVariable Long id, @RequestParam Long locationId) {
        BatchDetail batchDetail  = batchDetailService.updateBatchDetailForProductWithLocation(id, locationId);
        if (batchDetail != null) {
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
    public ResponseEntity<List<OrderQuantity>> updateQuantityForOrderInWarehouse(@PathVariable Long id, @RequestParam Integer quantity,  @RequestParam Long warehouseId) {
        List<OrderQuantity> updated = batchDetailService.updateQuantityForOrder(id, quantity, warehouseId);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/quantity/transfer")
    public ResponseEntity<List<OrderQuantity>> updateQuantityForOrder(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam Long warehouseId) {

        List<OrderQuantity> updatedOrders = batchDetailService.updateQuantityForOrder(productId, quantity, warehouseId);
        return ResponseEntity.ok(updatedOrders);
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
    @GetMapping("/quantity/{productId}/{warehouseId}")
    public Integer getQuantityByProductIdAndWarehouseId(@PathVariable Long productId, @PathVariable Long warehouseId) {
        return batchDetailService.getQuantityByIdProductId(productId, warehouseId);
    }
    @GetMapping("/lock/quantity/{productId}/{warehouseId}")
    public Integer getQuantityByProductIdAndWarehouseId_lock(@PathVariable Long productId, @PathVariable Long warehouseId) {
        return batchDetailService.getQuantityByIdProductId_lock(productId, warehouseId);
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
    public List<BatchDetail> getBatchDetailsByProductId(@PathVariable Long productId, @RequestParam Long warehouseId) {
        return batchDetailService.getBatchDetailsByProductId(productId, warehouseId);
    }
    @GetMapping("/top-lowest-quantity")
    public List<ProductQuantity> getTopLowestQuantity(@RequestParam int limit, @RequestParam Long warehouseId) {
        return batchDetailService.getTopNLowestQuantity(limit, warehouseId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteBatchDetailReturnBatchID(@RequestBody List<Long> listID) {
        boolean isDeleted = batchDetailService.deleteBatchDetailReturnBathID(listID);
        return ResponseEntity.ok(isDeleted);
    }
    @GetMapping("/batch/{batchId}")
    public List<BatchDetailDTO> getBatchDetailsByBatchId(@PathVariable Long batchId) {
        return batchDetailService.getBatchDetailsByBatchId(batchId);
    }
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ProductLocation>> getBatchDetailsByLocationId(@PathVariable Long locationId) {
                List<ProductLocation> productLocations = batchDetailService.getBatchDetailsByLocationId(locationId);
        return ResponseEntity.ok(productLocations);
    }

}
