package com.example.goodsservice.repository;

import com.example.goodsservice.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("SELECT w FROM Warehouse w WHERE w.id <> :id")
    List<Warehouse> findAllExcludingId(@Param("id") Long id);
}
