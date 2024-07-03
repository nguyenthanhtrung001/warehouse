package com.example.orderservice.service;

import com.example.orderservice.entity.Customer;

import java.util.List;

public interface  ICustomerService {

    Customer createCustomer(Customer customer);

    Customer getCustomerById(Long id);

    List<Customer> getAllCustomers();

    boolean updateCustomer(Long id, Customer customer);

    boolean deleteCustomer(Long id);
}
