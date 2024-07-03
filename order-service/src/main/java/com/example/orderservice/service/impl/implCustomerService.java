package com.example.orderservice.service.impl;

import com.example.orderservice.entity.Customer;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implCustomerService implements ICustomerService {

    @Autowired
    private  CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public boolean updateCustomer(Long id, Customer customer) {
        Optional<Customer> existingCustomerOpt = customerRepository.findById(id);
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setCustomerName(customer.getCustomerName());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setDateOfBirth(customer.getDateOfBirth());
            existingCustomer.setAddress(customer.getAddress());
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
}