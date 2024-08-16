package com.example.productservice.repository;

import com.example.productservice.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    Price findFirstByProductIdOrderByEffectiveDateDesc(Long productId);
    void deleteByProduct_Id(Long productId);
}
