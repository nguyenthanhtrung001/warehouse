package com.example.employeeservice.service;

import com.example.employeeservice.entity.Employee;

import java.util.List;

public interface IEmployeeService {
    Employee createEmployee(Employee employee);

    Employee getEmployeeById(Long id);

    List<Employee> getAllEmployees();

    boolean updateEmployee(Long id, Employee employee);

    boolean deleteEmployee(Long id);
}
