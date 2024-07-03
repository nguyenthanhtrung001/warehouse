package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.InventoryCheckSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryCheckSlipRepository extends JpaRepository<InventoryCheckSlip, Long> {
}
