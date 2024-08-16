package com.example.inventoryservice.repository;

import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.InventoryCheckDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryCheckDetailRepository extends JpaRepository<InventoryCheckDetail, Long> {
    void deleteAllByInventoryCheckSlipId(Long id);

    List<InventoryCheckDetail> findByInventoryCheckSlipId(Long id);

    @Query("SELECT new com.example.inventoryservice.dto.response.ProductQuantity(icd.batchDetail.productId, SUM(CASE WHEN icd.quantityDiscrepancy > 0 THEN icd.quantityDiscrepancy ELSE 0 END)) " +
            "FROM InventoryCheckDetail icd " +
            "JOIN InventoryCheckSlip ics ON icd.inventoryCheckSlip.id = ics.id " +
            "WHERE ics.inventoryCheckTime >= :startOfMonth AND ics.inventoryCheckTime <= :endOfMonth " +
            "GROUP BY icd.batchDetail.productId")
    List<ProductQuantity> findProductDiscrepanciesForMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                              @Param("endOfMonth") LocalDateTime endOfMonth);
    @Query("SELECT new com.example.inventoryservice.dto.response.ProductQuantity(icd.batchDetail.productId, SUM(CASE WHEN icd.quantityDiscrepancy < 0 THEN icd.quantityDiscrepancy ELSE 0 END)) " +
            "FROM InventoryCheckDetail icd " +
            "JOIN InventoryCheckSlip ics ON icd.inventoryCheckSlip.id = ics.id " +
            "WHERE ics.inventoryCheckTime >= :startOfMonth AND ics.inventoryCheckTime <= :endOfMonth " +
            "GROUP BY icd.batchDetail.productId")

    List<ProductQuantity> findProductDiscrepanciesLessForMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
