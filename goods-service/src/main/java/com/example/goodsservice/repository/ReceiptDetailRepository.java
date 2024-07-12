package com.example.goodsservice.repository;

import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {
    List<ReceiptDetail> findByReceipt(Receipt receipt);
}
