package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.InventoryCheckDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryCheckDetailRepository extends JpaRepository<InventoryCheckDetail, Long> {
    void deleteAllByInventoryCheckSlipId(Long id);
}
