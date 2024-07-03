package com.example.orderservice.repository;

import com.example.orderservice.entity.ReturnNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnNoteRepository extends JpaRepository<ReturnNote, Long> {
}
