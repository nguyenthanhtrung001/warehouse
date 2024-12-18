package com.example.orderservice.service;

import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.entity.Customer;

import java.util.List;

public interface  ICustomerService {

    Customer createCustomer(CustomerDTO customer);

    Customer getCustomerById(Long id);

    List<CustomerDTO> getAllCustomers();

    boolean updateCustomer(Long id, Customer customer);

    boolean deleteCustomer(Long id);
    public Customer getCustomerByEmail(String email);


}
