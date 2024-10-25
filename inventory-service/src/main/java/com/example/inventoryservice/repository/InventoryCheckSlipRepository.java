package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.InventoryCheckSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryCheckSlipRepository extends JpaRepository<InventoryCheckSlip, Long> {
    @Query("SELECT s FROM InventoryCheckSlip s WHERE s.warehouseId = :warehouseId")
    List<InventoryCheckSlip> findByWarehouseId(@Param("warehouseId") Long warehouseId);

}
