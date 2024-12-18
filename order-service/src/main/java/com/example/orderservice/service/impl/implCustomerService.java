package com.example.orderservice.service.impl;

import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.entity.ContactInfo;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.mapper.CustomerMapper;
import com.example.orderservice.repository.ContactInfoRepository;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.service.IContactInfoService;
import com.example.orderservice.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implCustomerService implements ICustomerService {

    @Autowired
    private  CustomerRepository customerRepository;
    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @Autowired
    private IContactInfoService iContactInfoService;

    @Transactional
    public Customer createCustomer(CustomerDTO customerDTO) {

        if (customerRepository.existsByPhoneNumber(customerDTO.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống.");
        }

        Customer cus = CustomerMapper.toEntity(customerDTO);

        ContactInfo contactInfo = new ContactInfo();
        Customer customer=  customerRepository.save(cus);

        contactInfo.setCustomer(customer);
        contactInfo.setProvince(customerDTO.getProvince());
        contactInfo.setDistrict(customerDTO.getDistrict());
        contactInfo.setWard(customerDTO.getWard());
        contactInfo.setStatus(1);
        contactInfo.setPhoneNumber(customerDTO.getPhoneNumber());
        contactInfo.setDetailedAddress(customerDTO.getDetailedAddress());
        contactInfo.setRecipientName(customerDTO.getCustomerName());

        contactInfoRepository.save(contactInfo);
        return cus;
    }

    @Override
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for( Customer customer: customers){
           CustomerDTO dto = CustomerMapper.toDTO(customer);
           String address = iContactInfoService.getContactInfoByCustomerId(customer.getId()).toString();
           dto.setDetailedAddress(address);
            customerDTOS.add(dto);
        }
        return customerDTOS;
    }

    @Override
    public boolean updateCustomer(Long id, Customer customer) {
        Optional<Customer> existingCustomerOpt = customerRepository.findById(id);
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setCustomerName(customer.getCustomerName());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setDateOfBirth(customer.getDateOfBirth());
//            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setNote(customer.getNote());
            customerRepository.save(existingCustomer);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}