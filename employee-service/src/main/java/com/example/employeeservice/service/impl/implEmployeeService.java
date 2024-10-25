package com.example.employeeservice.service.impl;

import com.example.employeeservice.client.IdentityClient;
import com.example.employeeservice.dto.request.ApiResponse;
import com.example.employeeservice.dto.request.UserCreationRequest;
import com.example.employeeservice.dto.response.UserResponse;
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
    @Autowired
    private IdentityClient identityClient;


    @Override
    public Employee createEmployee(Employee employee) {
        UserCreationRequest userCreationRequest = new UserCreationRequest();
        if(employee.getAccountId().equals("false"))
        {
            employee.setAccountId(null);
        }
        Employee  em = employeeRepository.save(employee);
        if (employee.getAccountId()==null) return  em;
        if (employee.getAccountId().equals("true")){
            try{
                String us = "NV000"+em.getId();
                userCreationRequest.setUsername(us);
                userCreationRequest.setPassword("123456");
                ApiResponse<UserResponse> response= identityClient.createUser(userCreationRequest);
                System.out.println("Response: " + response);
                em.setAccountId(us);
                updateEmployee(em.getId(),em);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return em;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    @Override
    public String getEmployeeNameById(Long id) {
        return employeeRepository.findEmployeeNameById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
       // return employeeRepository.findAll();
        return employeeRepository.findAllNonAdminEmployees();
    }
    @Override
    public List<Employee> getAllEmployees(Long warehouseId, Long employeeId) {
        return employeeRepository.findAllNonAdminEmployeesByWarehouseIdAndNotEmployeeId(warehouseId, employeeId);
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
            existingEmployee.setAccountId(employeeDetails.getAccountId());
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

    @Override
    public Employee getEmployeeByAccountId(String accountId) {
        return employeeRepository.findByAccountId(accountId);
    }
}
