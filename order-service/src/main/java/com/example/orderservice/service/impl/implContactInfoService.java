package com.example.orderservice.service.impl;

import com.example.orderservice.dto.ContactInfoDTO;
import com.example.orderservice.entity.ContactInfo;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.mapper.ContactInfoMapper;
import com.example.orderservice.repository.ContactInfoRepository;
import com.example.orderservice.service.IContactInfoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implContactInfoService implements IContactInfoService {

    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @Override
    public List<ContactInfo> getContactInfoByCustomerId(Long customerId) {
        return contactInfoRepository.findByCustomerId(customerId);
    }

    @Override
    public ContactInfo getContactInfoById(Long id) {
        Optional<ContactInfo> contactInfoOptional = contactInfoRepository.findById(id);
        return contactInfoOptional.orElse(null);
    }

    @Override
    public ContactInfo createContactInfo(ContactInfoDTO contactInfoDTO) {
        ContactInfo contactInfo = ContactInfoMapper.toEntity(contactInfoDTO);

        return contactInfoRepository.save(contactInfo);
    }
    @Override
    public ContactInfo updateContactInfo(Long id, ContactInfoDTO contactInfoDTO) {
        ContactInfo contactInfo = contactInfoRepository.findById(id).orElse(null);

        if (contactInfo != null) {
            // Kiểm tra các giá trị nhập vào từ contactInfoDTO trước khi gọi set
            if (contactInfoDTO.getRecipientName() != null && !contactInfoDTO.getRecipientName().isEmpty()) {
                contactInfo.setRecipientName(contactInfoDTO.getRecipientName());
            }
            if (contactInfoDTO.getPhoneNumber() != null && !contactInfoDTO.getPhoneNumber().isEmpty()) {
                contactInfo.setPhoneNumber(contactInfoDTO.getPhoneNumber());
            }
            if (contactInfoDTO.getWard() != null && !contactInfoDTO.getWard().isEmpty()) {
                contactInfo.setWard(contactInfoDTO.getWard());
            }
            if (contactInfoDTO.getDistrict() != null && !contactInfoDTO.getDistrict().isEmpty()) {
                contactInfo.setDistrict(contactInfoDTO.getDistrict());
            }
            if (contactInfoDTO.getProvince() != null && !contactInfoDTO.getProvince().isEmpty()) {
                contactInfo.setProvince(contactInfoDTO.getProvince());
            }
            if (contactInfoDTO.getDetailedAddress() != null && !contactInfoDTO.getDetailedAddress().isEmpty()) {
                contactInfo.setDetailedAddress(contactInfoDTO.getDetailedAddress());
            }

            // Tiến hành lưu lại đối tượng đã cập nhật
            return contactInfoRepository.save(contactInfo);
        } else {
            throw new EntityNotFoundException("Contact info not found for id: " + id);
        }
    }

}
