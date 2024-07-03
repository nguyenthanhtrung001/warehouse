package com.example.goodsservice.service;

import com.example.goodsservice.entity.DeliveryDetail;

import java.util.List;
import java.util.Optional;

public interface IDeliveryDetailService {

    DeliveryDetail createDeliveryDetail(DeliveryDetail deliveryDetail);

    Optional<DeliveryDetail> getDeliveryDetailById(Long id);

    List<DeliveryDetail> getAllDeliveryDetails();

    boolean updateDeliveryDetail(Long id, DeliveryDetail deliveryDetail);

    boolean deleteDeliveryDetail(Long id);
}
