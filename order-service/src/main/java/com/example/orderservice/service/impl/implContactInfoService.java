package com.example.orderservice.service.impl;

import com.example.orderservice.entity.ContactInfo;
import com.example.orderservice.repository.ContactInfoRepository;
import com.example.orderservice.service.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class implContactInfoService implements ContactInfoService {

    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @Override
    public List<ContactInfo> getContactInfoByCustomerId(Long customerId) {
        return contactInfoRepository.findByCustomerId(customerId);
    }
}
