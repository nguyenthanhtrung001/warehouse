package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.repository.BatchDetailRepository;
import com.example.inventoryservice.service.IBatchDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class implBatchDetailService implements IBatchDetailService {
    @Autowired
    private  BatchDetailRepository batchDetailRepository;


    @Override
    public BatchDetail createBatchDetail(BatchDetail batchDetail) {
        return batchDetailRepository.save(batchDetail);
    }

    @Override
    public BatchDetail getBatchDetailById(Long id) {
        Optional<BatchDetail> batchDetail = batchDetailRepository.findById(id);
        return batchDetail.orElse(null);
    }

    @Override
    public Long getQuantityByIdProductId(Long id) {
        return batchDetailRepository.getQuantityByProductId(id);
    }

    @Override
    public Long getQuantityByProductIdAndBatchId(Long productId, Long batchId) {
        return batchDetailRepository.getQuantityByProductIdAndBatchId(productId, batchId);

    }

    @Override
    public List<Long> getProductByBatchId(Long batchId) {
        return batchDetailRepository.getProductByBatchId(batchId);
    }

    @Override
    public List<Location> getLocationByProductId(Long productId) {
        return batchDetailRepository.findLocationsByProductId(productId);
    }

    @Override
    public List<BatchDetail> getAllBatchDetails() {
        return batchDetailRepository.findAll();
    }

    @Override
    public boolean updateBatchDetail(Long id, BatchDetail batchDetail) {
        if (batchDetailRepository.existsById(id)) {
            batchDetail.setId(id);
            batchDetailRepository.save(batchDetail);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateQuantityDeliveryDetail(Long id, Integer quantity) {
        BatchDetail detail= batchDetailRepository.findById(id).orElse(null);
        if (detail != null) {
            int quantityBath = detail.getQuantity();
            if(quantity>quantityBath) return false;
            int result = quantityBath - quantity;
            detail.setQuantity(result);
            batchDetailRepository.save(detail);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateQuantityForCheckInventory(Long id, Integer quantity) {

            BatchDetail detail= batchDetailRepository.findById(id).orElse(null);
            if (detail != null) {
                int quantityBath = detail.getQuantity();
                int result = quantityBath + quantity;
                detail.setQuantity(result);
                batchDetailRepository.save(detail);
                return true;
            }
            return false;

    }


    @Override
    public boolean deleteBatchDetail(Long id) {
        if (batchDetailRepository.existsById(id)) {
            batchDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
