package com.example.orderservice.controller;

import com.example.orderservice.entity.ContactInfo;
import com.example.orderservice.service.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-info")
public class ContactInfoController {

    @Autowired
    private ContactInfoService contactInfoService;

    @GetMapping("/customer/{customerId}")
    public List<ContactInfo> getContactInfoByCustomer(@PathVariable Long customerId) {
        return contactInfoService.getContactInfoByCustomerId(customerId);
    }
}
