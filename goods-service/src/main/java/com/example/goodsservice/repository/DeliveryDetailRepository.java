package com.example.goodsservice.repository;

import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.entity.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Long> {

    List<DeliveryDetail> findByDeliveryNoteId(Long node);
    @Query("SELECT dd FROM DeliveryDetail dd WHERE dd.deliveryNote.type = :type")
    List<DeliveryDetail> findAllByType(@Param("type") Integer type);
    @Query("SELECT dd FROM DeliveryDetail dd WHERE dd.deliveryNote.id = :deliveryNoteId AND dd.deliveryNote.type = :type")
    List<DeliveryDetail> findByDeliveryNoteIdAndType(@Param("deliveryNoteId") Long deliveryNoteId,@Param("type") Long type);


    @Query("SELECT new com.example.goodsservice.dto.response.ProductQuantity(dd.ProductId, SUM(dd.quantity)) " +
            "FROM DeliveryDetail dd " +
            "WHERE dd.deliveryNote.deliveryDate >= :startOfMonth AND dd.deliveryNote.deliveryDate <= :endOfMonth " +
            "AND dd.deliveryNote.type = :type " +
            "GROUP BY dd.ProductId")
    List<ProductQuantity> findProductQuantitiesForMonthAndType(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                               @Param("endOfMonth") LocalDateTime endOfMonth,
                                                               @Param("type") Integer type);

    @Query("SELECT SUM(dd.quantity) FROM DeliveryDetail dd WHERE dd.deliveryNote.receipt.id = :receiptId AND dd.batchDetail_Id = :batchDetailId")
    Integer findTotalQuantityByReceiptIdAndBatchDetailId(@Param("receiptId") Long receiptId, @Param("batchDetailId") Long batchDetailId);

    @Query("SELECT SUM(dd.quantity) FROM DeliveryDetail dd WHERE dd.deliveryNote.receipt.id = :receiptId")
    Integer findTotalQuantityByReceiptId(@Param("receiptId") Long receiptId);

    List<DeliveryDetail> findByDeliveryNote_Receipt_Supplier_Id(Long supplierId);
}