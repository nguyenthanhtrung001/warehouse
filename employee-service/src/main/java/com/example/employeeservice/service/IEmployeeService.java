package com.example.employeeservice.service;

import com.example.employeeservice.entity.Employee;

import java.util.List;

public interface IEmployeeService {
    Employee createEmployee(Employee employee);

    Employee getEmployeeById(Long id);
    public String getEmployeeNameById(Long id);

    List<Employee> getAllEmployees();
    public List<Employee> getAllEmployees(Long warehouseId, Long employeeId);

    boolean updateEmployee(Long id, Employee employee);

    boolean deleteEmployee(Long id);
    public Employee getEmployeeByAccountId(String accountId);
}
