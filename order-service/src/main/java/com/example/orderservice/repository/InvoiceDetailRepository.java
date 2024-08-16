package com.example.orderservice.repository;

import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
    //    void deleteByInvoiceId(Long invoiceId);
    List<InvoiceDetail> findByInvoiceId_Id(Long invoiceId);

    @Query("SELECT new com.example.orderservice.dto.response.ProductQuantity(d.productId, SUM(d.quantity)) " +
            "FROM InvoiceDetail d " +
            "GROUP BY d.productId " +
            "ORDER BY SUM(d.quantity) DESC")
    List<ProductQuantity> findProductQuantities(Pageable pageable);

    @Query("SELECT new com.example.orderservice.dto.response.ProductQuantity(id.productId, SUM(id.quantity)) " +
            "FROM InvoiceDetail id " +
            "WHERE id.invoiceId.printDate >= :startOfMonth AND id.invoiceId.printDate <= :endOfMonth " +
            "GROUP BY id.productId")
    List<ProductQuantity> findProductQuantitiesForMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                        @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT new com.example.orderservice.dto.response.ProductQuantity(id.productId, SUM(id.quantity)) " +
            "FROM InvoiceDetail id " +
            "WHERE id.invoiceId.printDate >= :startOfMonth AND id.invoiceId.printDate <= :endOfMonth " +
            "AND id.productId = :productId " +
            "GROUP BY id.productId")
    ProductQuantity findProductQuantityForMonthAndProduct(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                          @Param("endOfMonth") LocalDateTime endOfMonth,
                                                          @Param("productId") Long productId);


    @Query("SELECT i FROM InvoiceDetail i WHERE i.invoiceId.printDate BETWEEN :startDate AND :endDate")
    List<InvoiceDetail> findAllByYear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT id.note_return FROM InvoiceDetail id WHERE id.invoiceId.id = :invoiceId AND id.productId = :productId")
    String findNoteReturnByInvoiceIdAndProductId(@Param("invoiceId") Long invoiceId, @Param("productId") Long productId);
}