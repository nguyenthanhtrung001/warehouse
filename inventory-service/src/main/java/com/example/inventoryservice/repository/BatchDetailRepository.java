package com.example.inventoryservice.repository;

import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchDetailRepository extends JpaRepository<BatchDetail, Long> {
    List<BatchDetail> findByProductIdAndQuantityGreaterThan(Long productId, Integer quantity);

    @Query("SELECT SUM(b.quantity) FROM BatchDetail b WHERE b.productId = :productId")
    Integer getQuantityByProductId(Long productId);

    @Query("SELECT SUM(b.quantity) FROM BatchDetail b WHERE b.productId = :productId AND b.batch.warehouseId = :warehouseId")
    Integer getQuantityByProductIdWarehouse(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT SUM(b.quantity) FROM BatchDetail b WHERE b.productId = :productId AND b.batch.warehouseId = :warehouseId")
    Integer getQuantityByProductIdWarehouse_lock(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);


    @Query("SELECT bd.location FROM BatchDetail bd WHERE bd.productId = :productId")
    List<Location> findLocationsByProductId(Long productId);

    @Query("SELECT bd.quantity FROM BatchDetail bd WHERE bd.productId = :productId AND bd.batch.id = :batchId")
    Long getQuantityByProductIdAndBatchId(Long productId, Long batchId);

    @Query("SELECT bd.productId FROM BatchDetail bd WHERE bd.batch.id = :batchId")
    List<Long> getProductByBatchId(Long batchId);
    @Query("SELECT bd FROM BatchDetail bd WHERE bd.productId = :productId AND bd.quantity > 0 ")
    List<BatchDetail> findByProductIdAndQuantityGreaterThan(Long productId);
    @Query("SELECT bd FROM BatchDetail bd WHERE bd.productId = :productId AND bd.quantity > 0 AND bd.batch.warehouseId =:warehouseId")
    List<BatchDetail> findByProductIdAndQuantityGreaterThan(Long productId, Long warehouseId);
//    @Query("SELECT bd FROM BatchDetail bd WHERE bd.productId = :productId AND bd.quantity > 0 AND bd.batch.warehouseId =:warehouseId ")
//    List<BatchDetail> findByProductIdAndQuantityGreaterThanInWarehouseId(Long productId,Long warehouseId);
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT bd FROM BatchDetail bd WHERE bd.productId = :productId AND bd.quantity > 0 AND bd.batch.warehouseId = :warehouseId " +
        "AND (bd.batch.expiryDate IS NULL OR bd.batch.expiryDate >= CURRENT_DATE) " + // Điều kiện để loại bỏ expiryDate < hiện tại
        "ORDER BY CASE WHEN bd.batch.expiryDate IS NULL THEN 1 ELSE 0 END, bd.batch.expiryDate ASC")
List<BatchDetail> findByProductIdAndQuantityGreaterThanInWarehouseId(Long productId, Long warehouseId);

    @Query("SELECT new com.example.inventoryservice.dto.response.ProductQuantity(bd.productId, SUM(bd.quantity)) " +
            "FROM BatchDetail bd " +
            "JOIN bd.batch b " +
            "WHERE b.warehouseId = :warehouseId " +
            "GROUP BY bd.productId " +
            "ORDER BY SUM(bd.quantity) ASC")
    List<ProductQuantity> findAllInventoryProductAndQuantityForWarehouse(@Param("warehouseId") Long warehouseId);

    List<BatchDetail> findByProductId(Long productId);
    @Query("SELECT new com.example.inventoryservice.dto.response.ProductQuantity(bd.productId, SUM(bd.quantity)) " +
            "FROM BatchDetail bd " +
            "JOIN bd.batch b " +
            "WHERE b.warehouseId = :warehouseId " +
            "GROUP BY bd.productId")
    List<ProductQuantity> findProductQuantitiesByWarehouseId(@Param("warehouseId") Long warehouseId);


    List<BatchDetail> findByBatch_Id(Long batchId);

    List<BatchDetail> findByLocationId(Long locationId);
    @Query("SELECT new com.example.inventoryservice.dto.response.ProductQuantity(b.productId, SUM(b.quantity)) " +
            "FROM BatchDetail b " +
            "WHERE b.location.id = :locationId " +
            "GROUP BY b.productId")
    List<ProductQuantity> findSumQuantityByProductIdAndLocationId(Long locationId);

    @Query("SELECT COALESCE(SUM(bd.quantity), 0) FROM BatchDetail bd " +
            "WHERE bd.location.id = :locationId AND bd.location.warehouseId = :warehouseId")
    Long findTotalQuantityByWarehouseAndLocation(
            @Param("warehouseId") Long warehouseId,
            @Param("locationId") Long locationId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BatchDetail b WHERE b.id = :id")
    BatchDetail findBatchDetailForUpdate(@Param("id") Long id);
}