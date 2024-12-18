package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.dto.response.BatchDetailInfo;
import com.example.inventoryservice.dto.response.BatchLocation;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.Batch;

import java.util.List;

public interface  IBatchService {
    Batch createBatch(Batch batch);

    Batch getBatchById(Long id);

    List<Batch> getAllBatches();
    List<Batch> getAllBatchesForWarehouseId(Long warehouseId);

    boolean updateBatch(Long id, Batch batch);

    boolean deleteBatch(Long id);
    public BatchLocation getBatchLocatonForProduct(Long productId, Long warehouseId);
    public List<Long> getProductIdsWithBatchesExpiringIn7Days();

    public List<Long> getExpiredProductIds(Long warehouseId);
    public List<ProductResponse> getProductsByWarehouseId(Long id);
    public List<BatchDetailInfo> getBatchDetailsByWarehouseId(Long warehouseId);


    }
