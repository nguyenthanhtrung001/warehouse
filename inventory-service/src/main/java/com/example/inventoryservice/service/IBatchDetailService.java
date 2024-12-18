package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.response.BatchDetailDTO;
import com.example.inventoryservice.dto.response.OrderQuantity;
import com.example.inventoryservice.dto.response.ProductLocation;
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
    Integer getQuantityByIdProductId(Long productId, Long wareHouse );
    public Integer getQuantityByIdProductId_lock(Long productId, Long warehouseId);
    Long getQuantityByProductIdAndBatchId(Long productId, Long batchId);
    public List<Long> getProductByBatchId(Long batchId);
    public List<BatchDetail> getBatchDetailsByProductId(Long productId);
    public List<BatchDetail> getBatchDetailsByProductId(Long productId,Long warehouseId);

    public List<Location> getLocationByProductId(Long productId);
    List<BatchDetail> getAllBatchDetails();

    boolean updateBatchDetail(Long id, BatchDetail batchDetail);
    BatchDetail updateBatchDetailForProductWithLocation(Long id, Long locationId);
    boolean updateQuantityDeliveryDetail(Long id, Integer quantity);

    boolean updateQuantityForCheckInventory(Long id, Integer quantity);
    boolean updateQuantityForReturnOrder(Long id, Integer quantity);
    List<OrderQuantity> updateQuantityForOrder(Long productId, Integer quantity);
    List<OrderQuantity> updateQuantityForOrder(Long productId, Integer quantity, Long WarehouseId);
    public List<ProductQuantity> getTopNLowestQuantity(int limit, Long warehouseId);
    boolean deleteBatchDetailReturnBathID(List<Long>ListID);
    boolean deleteBatchDetail(Long id);
    public List<BatchDetailDTO> getBatchDetailsByBatchId(Long batchId);
    public List<ProductLocation> getBatchDetailsByLocationId(Long locationId);
    public Long getTotalQuantityByWarehouseAndLocation(Long warehouseId, Long locationId);
}
