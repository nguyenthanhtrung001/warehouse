package com.example.orderservice.controller;

import com.example.orderservice.dto.ContactInfoDTO;
import com.example.orderservice.entity.ContactInfo;
import com.example.orderservice.service.IContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-info")
public class ContactInfoController {

    @Autowired
    private IContactInfoService IContactInfoService;

    @GetMapping("/customer/{customerId}")
    public List<ContactInfo> getContactInfoByCustomer(@PathVariable Long customerId) {
        return IContactInfoService.getContactInfoByCustomerId(customerId);
    }
    @PostMapping
    public ResponseEntity<ContactInfo> createContactInfo(@RequestBody ContactInfoDTO contactInfoDTO) {
        ContactInfo createdContactInfo = IContactInfoService.createContactInfo(contactInfoDTO);
        return new ResponseEntity<>(createdContactInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInfo> updateContactInfo(@PathVariable Long id, @RequestBody ContactInfoDTO contactInfoDTO) {
        try {
            // Gọi service để cập nhật thông tin
            ContactInfo updatedContactInfo = IContactInfoService.updateContactInfo(id, contactInfoDTO);

            // Nếu không tìm thấy đối tượng, trả về mã lỗi 404
            if (updatedContactInfo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Trả về thông tin đã cập nhật
            return new ResponseEntity<>(updatedContactInfo, HttpStatus.OK);
        } catch (Exception e) {
            // Xử lý trường hợp ngoại lệ, trả về lỗi 500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
