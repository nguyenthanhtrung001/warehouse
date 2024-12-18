package com.example.inventoryservice.repository;

import com.example.inventoryservice.dto.response.BatchDetailInfo;
import com.example.inventoryservice.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    @Query("SELECT new com.example.inventoryservice.dto.response.BatchDetailInfo(b.batchName, bd.productId, SUM(bd.quantity), l.warehouseLocation, b.expiryDate, MIN(bd.id)) " +
            "FROM Batch b " +
            "JOIN b.batchDetails bd " +
            "JOIN bd.location l " +
            "WHERE b.warehouseId = :warehouseId " +
            "GROUP BY b.batchName, bd.productId, l.warehouseLocation, b.expiryDate")
    List<BatchDetailInfo> findBatchDetailsByWarehouseId(@Param("warehouseId") Long warehouseId);

    List<Batch> findByWarehouseId(Long warehouseId);
}