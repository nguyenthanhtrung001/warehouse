package com.example.orderservice.repository;

import com.example.orderservice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPrintDateBetweenAndWarehouseId(LocalDateTime startDateTime, LocalDateTime endDateTime, Long warehouseId);

    @Query("SELECT i FROM Invoice i WHERE i.status = :status AND i.warehouseId = :warehouseId")
    List<Invoice> findByStatusAndWarehouseId(@Param("status") int status, @Param("warehouseId") Long warehouseId);

    List<Invoice> findByCustomerId(Long customerId);

    @Query("SELECT i FROM Invoice i WHERE i.status NOT IN (0, 1) AND i.warehouseId = :warehouseId")
    List<Invoice> findAllInvoicesWithStatusNotInZeroOrOne(@Param("warehouseId") Long warehouseId);

}