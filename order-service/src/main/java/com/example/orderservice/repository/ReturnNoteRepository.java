package com.example.orderservice.repository;

import com.example.orderservice.entity.ReturnNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnNoteRepository extends JpaRepository<ReturnNote, Long> {
    List<ReturnNote> findByReturnDateBetween(LocalDate startOfWeek, LocalDate endOfWeek);

    long countByReturnDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT rn FROM ReturnNote rn WHERE rn.invoice.id = :invoiceId")
    ReturnNote findByInvoiceId(@Param("invoiceId") Long invoiceId);
}
