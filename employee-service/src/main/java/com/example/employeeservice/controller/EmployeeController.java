package com.example.employeeservice.controller;

import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }
    @PostMapping("/create-account")
    public ResponseEntity<Employee> createAccountEmployee(@RequestParam Long employeeId) {
        Employee createdEmployee = employeeService.createAccountEmployee(employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }
    @PutMapping("/update-account/{id}")
    public ResponseEntity<String> updateAccountEmployee(@PathVariable Long id) {
        boolean isUpdated = employeeService.updateAccountEmployee(id);

        if (isUpdated) {
            return ResponseEntity.ok("Account ID updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Employee not found.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/warehouse")
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(value = "warehouseId", required = false) Long warehouseId,
            @RequestParam(value = "employeeId", required = false) Long employeeId) {

        List<Employee> employees;

        if (warehouseId == null || warehouseId == 0) {
            // Lấy tất cả nhân viên nếu không có warehouseId hoặc warehouseId = 0
            employees = employeeService.getAllEmployees();
        } else {
            // Lấy danh sách nhân viên theo warehouseId và employeeId (nếu có)
            employees = employeeService.getAllEmployees(warehouseId, employeeId);
        }

        return ResponseEntity.ok(employees);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        boolean updated = employeeService.updateEmployee(id, employee);
        if (updated) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Nhân viên đã được xóa (cập nhật trạng thái về 0).");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/account/{accountId}")
    public Employee getEmployeeByAccountId(@PathVariable String accountId) {
        return employeeService.getEmployeeByAccountId(accountId);
    }
    @GetMapping("/{id}/name")
    public String getEmployeeNameById(@PathVariable Long id) {
        return employeeService.getEmployeeNameById(id);
    }
    @GetMapping("/warehouse/{warehouseId}/accountIds")
    public List<String> getAccountIdsByWarehouseId(@PathVariable Long warehouseId) {
        return employeeService.getAccountIdsByWarehouseId(warehouseId);
    }
}
