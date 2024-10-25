package com.example.goodsservice.repository;

import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {
    List<ReceiptDetail> findByReceipt(Receipt receipt);
    List<ReceiptDetail> findByReceiptId(Long receiptId);

    @Query("SELECT new com.example.goodsservice.dto.response.ProductQuantity(rd.ProductId, SUM(rd.quantity)) " +
            "FROM ReceiptDetail rd " +
            "WHERE rd.receipt.receiptDate >= :startOfMonth AND rd.receipt.receiptDate <= :endOfMonth " +
            "GROUP BY rd.ProductId")
    List<ProductQuantity> findProductQuantitiesForCurrentMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                               @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT new com.example.goodsservice.dto.response.ProductQuantity(rd.ProductId, SUM(rd.quantity)) " +
            "FROM ReceiptDetail rd " +
            "JOIN rd.receipt r " +
            "WHERE r.receiptDate >= :startOfMonth AND r.receiptDate <= :endOfMonth " +
            "AND r.warehouse.id = :warehouseId " + // Điều kiện để lọc theo warehouseId
            "GROUP BY rd.ProductId")
    List<ProductQuantity> findProductQuantitiesForCurrentMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                               @Param("endOfMonth") LocalDateTime endOfMonth,
                                                               @Param("warehouseId") Long warehouseId);
    @Query("SELECT SUM(rd.quantity) FROM ReceiptDetail rd WHERE rd.receipt.id = :receiptId")
    Integer findTotalQuantityByReceiptId(@Param("receiptId") Long receiptId);

    List<ReceiptDetail> findByReceipt_Supplier_IdAndReceipt_Warehouse_Id(Long supplierId, Long warehouseId);


    @Query("SELECT COUNT(r) > 0 FROM ReceiptDetail r WHERE r.ProductId = :productId")
    boolean existsByProductId(@Param("productId") Long productId);
}
