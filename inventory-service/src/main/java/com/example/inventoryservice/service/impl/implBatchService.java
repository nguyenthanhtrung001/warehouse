package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.repository.BatchRepository;
import com.example.inventoryservice.service.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implBatchService implements IBatchService {

    @Autowired
    private  BatchRepository batchRepository;


    @Override
    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public Batch getBatchById(Long id) {
        Optional<Batch> batch = batchRepository.findById(id);
        return batch.orElse(null);
    }

    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    @Override
    public boolean updateBatch(Long id, Batch batch) {
        if (batchRepository.existsById(id)) {
            batch.setId(id);
            batchRepository.save(batch);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBatch(Long id) {
        if (batchRepository.existsById(id)) {
            batchRepository.deleteById(id);
            return true;
        }
        return false;
    }
}