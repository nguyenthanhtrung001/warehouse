package com.example.goodsservice.service.impl;

import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.repository.DeliveryDetailRepository;
import com.example.goodsservice.service.IDeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implDeliveryDetailService implements IDeliveryDetailService {
     @Autowired
     DeliveryDetailRepository deliveryDetailRepository;

    @Override
    public DeliveryDetail createDeliveryDetail(DeliveryDetail deliveryDetail) {
        return deliveryDetailRepository.save(deliveryDetail);
    }

    @Override
    public Optional<DeliveryDetail> getDeliveryDetailById(Long id) {
        return deliveryDetailRepository.findById(id);
    }

    @Override
    public List<DeliveryDetail> getAllDeliveryDetails() {
        return deliveryDetailRepository.findAll();
    }

    @Override
    public boolean updateDeliveryDetail(Long id, DeliveryDetail deliveryDetail) {
        if (deliveryDetailRepository.existsById(id)) {
            deliveryDetail.setQuantity(deliveryDetail.getQuantity());
           deliveryDetail.setBatchDetail_Id(deliveryDetail.getBatchDetail_Id());

            deliveryDetailRepository.save(deliveryDetail);
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteDeliveryDetail(Long id) {
        if (deliveryDetailRepository.existsById(id)) {
            deliveryDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}