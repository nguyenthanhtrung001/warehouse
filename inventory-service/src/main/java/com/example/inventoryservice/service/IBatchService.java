package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Batch;

import java.util.List;

public interface  IBatchService {
    Batch createBatch(Batch batch);

    Batch getBatchById(Long id);

    List<Batch> getAllBatches();

    boolean updateBatch(Long id, Batch batch);

    boolean deleteBatch(Long id);
}
