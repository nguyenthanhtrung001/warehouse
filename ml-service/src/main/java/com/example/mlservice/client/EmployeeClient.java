package com.example.mlservice.client;
import com.example.mlservice.dto.response.Employee;
import com.example.mlservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "employee-service", url = "http://localhost:8085")
public interface EmployeeClient {

    @GetMapping (value = "/api/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Employee> getEmployeeById(@PathVariable Long id);
}
