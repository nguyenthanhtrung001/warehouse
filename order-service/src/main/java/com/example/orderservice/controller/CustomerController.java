package com.example.orderservice.controller;

import com.example.orderservice.dto.CustomerDTO;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.exception.DuplicateFieldException;
import com.example.orderservice.service.ICustomerService;
import com.example.orderservice.validator.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        // Validate customerDTO
        List<String> validationErrors = CustomerValidator.validate(customerDTO);
        if (!validationErrors.isEmpty()) {
            // Nếu có lỗi validation, trả về lỗi
            String errorMessage = String.join(", ", validationErrors);
            ApiResponse<Customer> apiResponse = new ApiResponse<>(false, "Validation failed: " + errorMessage, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        try {
            // Nếu dữ liệu hợp lệ, tạo customer
            Customer createdCustomer = customerService.createCustomer(customerDTO);

            // Trả về thông báo thành công
            ApiResponse<Customer> apiResponse = new ApiResponse<>(true, "Customer created successfully", createdCustomer);
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (DuplicateFieldException e) {
            // Nếu trùng số điện thoại hoặc email, trả về lỗi
            ApiResponse<Customer> apiResponse = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            // Nếu có lỗi khác, trả về lỗi chung
            ApiResponse<Customer> apiResponse = new ApiResponse<>(false, "An unexpected error occurred: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        boolean updated = customerService.updateCustomer(id, customer);
        if (updated) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/email")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();  // Trả về 404 nếu không tìm thấy
        }
    }
}
