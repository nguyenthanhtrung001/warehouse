package com.example.orderservice.repository;

import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.ReturnDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
public interface ReturnDetailRepository extends JpaRepository<ReturnDetail, Long> {
    @Query("SELECT new com.example.orderservice.dto.response.ProductQuantity(rd.productId, SUM(rd.quantity)) " +
            "FROM ReturnDetail rd " +
            "JOIN ReturnNote rn ON rd.returnNote = rn.id " +
            "WHERE rn.returnDate >= :startOfMonth AND rn.returnDate <= :endOfMonth " +
            "GROUP BY rd.productId")
    List<ProductQuantity> findProductQuantitiesForMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                                        @Param("endOfMonth") LocalDate endOfMonth);




    @Query("SELECT rd FROM ReturnDetail rd WHERE rd.returnNote IN (SELECT rn.id FROM ReturnNote rn WHERE rn.invoice.id = :invoiceId)")
    List<ReturnDetail> findByInvoiceId(@Param("invoiceId") Long invoiceId);
}