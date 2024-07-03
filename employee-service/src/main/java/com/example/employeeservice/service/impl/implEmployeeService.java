package com.example.employeeservice.service.impl;

import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implEmployeeService implements IEmployeeService {

    @Autowired
    private  EmployeeRepository employeeRepository;


    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public boolean updateEmployee(Long id, Employee employeeDetails) {
        Optional<Employee> existingEmployeeOpt = employeeRepository.findById(id);
        if (existingEmployeeOpt.isPresent()) {
            Employee existingEmployee = existingEmployeeOpt.get();
            existingEmployee.setEmployeeName(employeeDetails.getEmployeeName());
            existingEmployee.setBasicSalary(employeeDetails.getBasicSalary());
            existingEmployee.setGender(employeeDetails.getGender());
            existingEmployee.setDateOfBirth(employeeDetails.getDateOfBirth());
            existingEmployee.setPhoneNumber(employeeDetails.getPhoneNumber());
            existingEmployee.setImage(employeeDetails.getImage());
            existingEmployee.setDateJoined(employeeDetails.getDateJoined());
            existingEmployee.setPosition(employeeDetails.getPosition());
            existingEmployee.setAddress(employeeDetails.getAddress());
            existingEmployee.setEmail(employeeDetails.getEmail());
            existingEmployee.setStatus(employeeDetails.getStatus());
            employeeRepository.save(existingEmployee);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
