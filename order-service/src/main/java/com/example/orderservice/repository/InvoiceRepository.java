package com.example.orderservice.repository;

import com.example.orderservice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPrintDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Invoice> findByStatus(int i);

    @Query("SELECT i FROM Invoice i WHERE i.status NOT IN (0, 1)")
    List<Invoice> findAllInvoicesWithStatusNotInZeroOrOne();
}