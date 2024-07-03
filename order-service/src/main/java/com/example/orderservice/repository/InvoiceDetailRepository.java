package com.example.orderservice.repository;

import com.example.orderservice.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
    void deleteByInvoiceId(Long invoiceId);
}