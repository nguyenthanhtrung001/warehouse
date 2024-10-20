package com.example.orderservice.service;

import com.example.orderservice.entity.ContactInfo;

import java.util.List;

public interface ContactInfoService {
    public List<ContactInfo> getContactInfoByCustomerId(Long customerId);
}
