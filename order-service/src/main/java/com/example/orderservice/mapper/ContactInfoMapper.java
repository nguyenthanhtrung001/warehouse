package com.example.orderservice.mapper;

import com.example.orderservice.dto.ContactInfoDTO;
import com.example.orderservice.entity.ContactInfo;
import com.example.orderservice.entity.Customer;

public class ContactInfoMapper {

    // Convert ContactInfoDTO to ContactInfo entity
    public static ContactInfo toEntity(ContactInfoDTO contactInfoDTO) {
        if (contactInfoDTO == null) {
            return null;
        }
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(contactInfoDTO.getId());
        contactInfo.setRecipientName(contactInfoDTO.getRecipientName());
        contactInfo.setPhoneNumber(contactInfoDTO.getPhoneNumber());
        contactInfo.setProvince(contactInfoDTO.getProvince());
        contactInfo.setDistrict(contactInfoDTO.getDistrict());
        contactInfo.setWard(contactInfoDTO.getWard());
        contactInfo.setDetailedAddress(contactInfoDTO.getDetailedAddress());
        contactInfo.setStatus(0);
        contactInfo.setCustomer(new Customer(contactInfoDTO.getCustomerId()));
        return contactInfo;
    }

    // Convert ContactInfo entity to ContactInfoDTO
    public static ContactInfoDTO toDTO(ContactInfo contactInfo) {
        if (contactInfo == null) {
            return null;
        }
        ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
        contactInfoDTO.setId(contactInfo.getId());
        contactInfoDTO.setRecipientName(contactInfo.getRecipientName());
        contactInfoDTO.setPhoneNumber(contactInfo.getPhoneNumber());
        contactInfoDTO.setProvince(contactInfo.getProvince());
        contactInfoDTO.setDistrict(contactInfo.getDistrict());
        contactInfoDTO.setWard(contactInfo.getWard());
        contactInfoDTO.setDetailedAddress(contactInfo.getDetailedAddress());
        contactInfoDTO.setStatus(contactInfo.getStatus());
        return contactInfoDTO;
    }
}
