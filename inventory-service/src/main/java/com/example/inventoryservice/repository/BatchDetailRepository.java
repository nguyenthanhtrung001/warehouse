package com.example.inventoryservice.repository;

import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchDetailRepository extends JpaRepository<BatchDetail, Long> {
    List<BatchDetail> findByProductIdAndQuantityGreaterThan(Long productId, Integer quantity);

    @Query("SELECT SUM(b.quantity) FROM BatchDetail b WHERE b.productId = :productId")
    Integer getQuantityByProductId(Long productId);

    @Query("SELECT bd.location FROM BatchDetail bd WHERE bd.productId = :productId")
    List<Location> findLocationsByProductId(Long productId);

    @Query("SELECT bd.quantity FROM BatchDetail bd WHERE bd.productId = :productId AND bd.batch.id = :batchId")
    Long getQuantityByProductIdAndBatchId(Long productId, Long batchId);

    @Query("SELECT bd.productId FROM BatchDetail bd WHERE bd.batch.id = :batchId")
    List<Long> getProductByBatchId(Long batchId);
    @Query("SELECT bd FROM BatchDetail bd WHERE bd.productId = :productId AND bd.quantity > 0")
    List<BatchDetail> findByProductIdAndQuantityGreaterThan(Long productId);

    @Query("SELECT new com.example.inventoryservice.dto.response.ProductQuantity(b.productId, SUM(b.quantity)) " +
            "FROM BatchDetail b " +
            "GROUP BY b.productId " +
            "ORDER BY SUM(b.quantity) ASC")
    List<ProductQuantity> findAllOrderedByQuantity();

    List<BatchDetail> findByProductId(Long productId);
}