package com.example.goodsservice.repository;

import com.example.goodsservice.entity.DeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> {
    @Query("SELECT dn FROM DeliveryNote dn WHERE dn.type = :type")
    List<DeliveryNote> findAllByType(@Param("type") Integer type);
}