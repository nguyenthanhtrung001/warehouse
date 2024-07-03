package com.example.orderservice.repository;

import com.example.orderservice.entity.ReturnDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnDetailRepository extends JpaRepository<ReturnDetail, Long> {
}