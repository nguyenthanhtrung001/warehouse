package com.example.orderservice.service;

import com.example.orderservice.dto.ContactInfoDTO;
import com.example.orderservice.entity.ContactInfo;

import java.util.List;

public interface IContactInfoService {
    public List<ContactInfo> getContactInfoByCustomerId(Long customerId);
    public ContactInfo getContactInfoById(Long id);
    public ContactInfo createContactInfo(ContactInfoDTO contactInfoDTO);
    public ContactInfo updateContactInfo(Long id, ContactInfoDTO contactInfoDTO);
    public boolean deleteContactInfo(Long id);
}
