package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchDetailRepository extends JpaRepository<BatchDetail, Long> {
    @Query("SELECT SUM(b.quantity) FROM BatchDetail b WHERE b.productId = :productId")
    Long getQuantityByProductId(Long productId);

    @Query("SELECT bd.location FROM BatchDetail bd WHERE bd.productId = :productId")
    List<Location> findLocationsByProductId(Long productId);

    @Query("SELECT bd.quantity FROM BatchDetail bd WHERE bd.productId = :productId AND bd.batch.id = :batchId")
    Long getQuantityByProductIdAndBatchId(Long productId, Long batchId);

    @Query("SELECT bd.productId FROM BatchDetail bd WHERE bd.batch.id = :batchId")
    List<Long> getProductByBatchId(Long batchId);


}