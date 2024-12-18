package com.example.employeeservice.service.impl;

import com.example.employeeservice.client.IdentityClient;
import com.example.employeeservice.dto.request.ApiResponse;
import com.example.employeeservice.dto.request.UserCreationRequest;
import com.example.employeeservice.dto.response.UserResponse;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.IEmployeeService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
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


    @Transactional
    public Employee createEmployee(Employee employee) {
        System.out.println("vi tri:"+ employee.getPosition());
        UserCreationRequest userCreationRequest = new UserCreationRequest();
        if(employee.getAccountId().equals("false"))
        {
            employee.setAccountId(null);
        }
        Employee  em = employeeRepository.save(employee);
        if (employee.getAccountId()==null) return  em;
        if (employee.getAccountId().equals("true")){

                String us = "NV000"+em.getId();
               em.setAccountId(us);
                userCreationRequest.setUsername(us);
                userCreationRequest.setPassword("123456");
            if (em.getPosition().toLowerCase().contains("nhân viên")){
                userCreationRequest.setType(1);
                ApiResponse<UserResponse> response= identityClient.createUser(userCreationRequest);
                employeeRepository.save(em);
            }
            if (em.getPosition().toLowerCase().contains("quản lý")){
                userCreationRequest.setType(2);
                ApiResponse<UserResponse> response= identityClient.createUser(userCreationRequest);
                employeeRepository.save(em);
            }

                return  em;

        }
        return null;
    }

    @Transactional
    public Employee createAccountEmployee(Long employeeId) {
        Employee em = getEmployeeById(employeeId);
        UserCreationRequest userCreationRequest = new UserCreationRequest();
        if ( em!=null ){
            String us = "NV000"+em.getId();
            userCreationRequest.setUsername(us);
            userCreationRequest.setPassword("123456");
            if (em.getPosition().toLowerCase().contains("nhân viên"))
            {
                System.out.println("Tạo nhân viên");
                userCreationRequest.setType(1);
                ApiResponse<UserResponse> response= identityClient.createUser(userCreationRequest);
                em.setAccountId(us);
                updateEmployee(em.getId(),em);
            }
            if (em.getPosition().toLowerCase().contains("quản lý"))
            {
                System.out.println("Tạ oquản lý");
                userCreationRequest.setType(2);
                ApiResponse<UserResponse> response= identityClient.createUser(userCreationRequest);
                em.setAccountId(us);
                updateEmployee(em.getId(),em);
            }
            return em;

        }
        return null;

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

    public boolean updateAccountEmployee(Long id) {
        Optional<Employee> existingEmployeeOpt = employeeRepository.findById(id);
        if (existingEmployeeOpt.isPresent()) {
            Employee existingEmployee = existingEmployeeOpt.get();
            existingEmployee.setAccountId(null); // Set AccountId to null
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

    @Override
    public List<String> getAccountIdsByWarehouseId(Long warehouseId) {
        return employeeRepository.findAccountIdsByWarehouseId(warehouseId);
    }
}
