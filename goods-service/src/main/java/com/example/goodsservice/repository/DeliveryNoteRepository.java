package com.example.goodsservice.repository;

import com.example.goodsservice.entity.DeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> {
    @Query("SELECT dn FROM DeliveryNote dn WHERE dn.type = :type AND dn.warehouseSource.id = :warehouseId")
    List<DeliveryNote> findAllByTypeAndWarehouseId(@Param("type") Integer type, @Param("warehouseId") Long warehouseId);
    @Query("SELECT dn FROM DeliveryNote dn WHERE dn.type = :type AND dn.warehouseDestination.id = :warehouseId")
    List<DeliveryNote> findAllImportTransferByTypeAndWarehouseId(@Param("type") Integer type, @Param("warehouseId") Long warehouseId);

    @Query("SELECT COUNT(dn) FROM DeliveryNote dn WHERE dn.type = :type AND dn.status = :status")
    Long countByTypeAndStatus(@Param("type") Integer type, @Param("status") Integer status);

}