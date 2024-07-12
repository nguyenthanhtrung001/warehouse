package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;

import java.util.List;

public interface  IBatchDetailService {
    BatchDetail createBatchDetail(BatchDetail batchDetail);

    BatchDetail getBatchDetailById(Long id);
    Long getQuantityByIdProductId(Long id);
    Long getQuantityByProductIdAndBatchId(Long productId, Long batchId);
    public List<Long> getProductByBatchId(Long batchId);

    public List<Location> getLocationByProductId(Long productId);
    List<BatchDetail> getAllBatchDetails();

    boolean updateBatchDetail(Long id, BatchDetail batchDetail);
    boolean updateQuantityDeliveryDetail(Long id, Integer quantity);

    boolean deleteBatchDetail(Long id);
}
