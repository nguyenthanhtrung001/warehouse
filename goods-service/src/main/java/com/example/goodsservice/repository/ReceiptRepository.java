package com.example.goodsservice.repository;

import com.example.goodsservice.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    @Query("SELECT r FROM Receipt r WHERE r.receiptDate BETWEEN :startOfMonth AND :endOfMonth")
    List<Receipt> findAllReceiptsInMonth(
            @Param("startOfMonth") LocalDateTime startOfMonth,
            @Param("endOfMonth") LocalDateTime endOfMonth
    );

    @Query("SELECT r FROM Receipt r WHERE r.status <> 0")
    List<Receipt> findAllByStatusNotZero();
    @Query("SELECT r FROM Receipt r WHERE r.status <> 0 AND r.status <> 3")
    List<Receipt> findAllByStatusNotZeroAndNotThree();
}