package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.response.OrderQuantity;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;

import java.util.List;

public interface  IBatchDetailService {
    BatchDetail createBatchDetail(BatchDetail batchDetail);
    Batch getBatchByBatchDetailById(Long id);
    String getProductByBatchDetailById( Long id);
    BatchDetail getBatchDetailById(Long id);
    Integer getQuantityByIdProductId(Long id);
    Long getQuantityByProductIdAndBatchId(Long productId, Long batchId);
    public List<Long> getProductByBatchId(Long batchId);
    public List<BatchDetail> getBatchDetailsByProductId(Long productId);

    public List<Location> getLocationByProductId(Long productId);
    List<BatchDetail> getAllBatchDetails();

    boolean updateBatchDetail(Long id, BatchDetail batchDetail);
    boolean updateQuantityDeliveryDetail(Long id, Integer quantity);
    boolean updateQuantityForCheckInventory(Long id, Integer quantity);

    boolean updateQuantityForReturnOrder(Long id, Integer quantity);

    List<OrderQuantity> updateQuantityForOrder(Long id, Integer quantity);
    public List<ProductQuantity> getTopNLowestQuantity(int limit);

    boolean deleteBatchDetailReturnBathID(List<Long>ListID);
    boolean deleteBatchDetail(Long id);
}
