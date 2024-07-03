package com.example.orderservice.service.impl;

import com.example.orderservice.entity.ReturnDetail;
import com.example.orderservice.repository.ReturnDetailRepository;
import com.example.orderservice.service.IReturnDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implReturnDetailService implements IReturnDetailService {
    @Autowired
    private  ReturnDetailRepository returnDetailRepository;

    @Override
    public ReturnDetail createReturnDetail(ReturnDetail returnDetail) {
        return returnDetailRepository.save(returnDetail);
    }

    @Override
    public ReturnDetail getReturnDetailById(Long id) {
        Optional<ReturnDetail> returnDetail = returnDetailRepository.findById(id);
        return returnDetail.orElse(null);
    }

    @Override
    public List<ReturnDetail> getAllReturnDetails() {
        return returnDetailRepository.findAll();
    }

    @Override
    public boolean updateReturnDetail(Long id, ReturnDetail returnDetailDetails) {
        Optional<ReturnDetail> existingReturnDetailOpt = returnDetailRepository.findById(id);
        if (existingReturnDetailOpt.isPresent()) {
            ReturnDetail existingReturnDetail = existingReturnDetailOpt.get();
            existingReturnDetail.setReturnNote(returnDetailDetails.getReturnNote());
            existingReturnDetail.setProductId(returnDetailDetails.getProductId());
            existingReturnDetail.setQuantity(returnDetailDetails.getQuantity());
            returnDetailRepository.save(existingReturnDetail);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReturnDetail(Long id) {
        if (returnDetailRepository.existsById(id)) {
            returnDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
