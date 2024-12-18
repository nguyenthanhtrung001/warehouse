package com.example.orderservice.mapper;

import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.ContactInfo;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {

    // Chuyển đổi từ Customer sang CustomerDTO
    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setCustomerName(customer.getCustomerName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setDateOfBirth(customer.getDateOfBirth());

        customerDTO.setEmail(customer.getEmail());
        customerDTO.setNote(customer.getNote());


        return customerDTO;
    }

    // Chuyển đổi từ CustomerDTO sang Customer
    public static Customer toEntity(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setEmail(customerDTO.getEmail());
        customer.setNote(customerDTO.getNote());


        return customer;
    }

    // Chuyển đổi từ danh sách Customer sang danh sách CustomerDTO
    public static List<CustomerDTO> toDTOList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Chuyển đổi từ danh sách CustomerDTO sang danh sách Customer
    public static List<Customer> toEntityList(List<CustomerDTO> customerDTOs) {
        return customerDTOs.stream()
                .map(CustomerMapper::toEntity)
                .collect(Collectors.toList());
    }
}
