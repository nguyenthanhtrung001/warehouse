package com.example.orderservice.service;

import com.example.orderservice.entity.ReturnDetail;

import java.util.List;

public interface IReturnDetailService {
    ReturnDetail createReturnDetail(ReturnDetail returnDetail);

    ReturnDetail getReturnDetailById(Long id);

    List<ReturnDetail> getAllReturnDetails();

    boolean updateReturnDetail(Long id, ReturnDetail returnDetail);

    boolean deleteReturnDetail(Long id);
}
