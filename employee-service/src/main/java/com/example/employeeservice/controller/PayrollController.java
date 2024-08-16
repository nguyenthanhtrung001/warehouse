package com.example.employeeservice.controller;

import com.example.employeeservice.dto.response.PayrollSummaryResponse;
import com.example.employeeservice.entity.Payroll;
import com.example.employeeservice.service.IPayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired
    private  IPayrollService payrollService;

    @PostMapping
    public ResponseEntity<Payroll> createPayroll(@RequestBody Payroll payroll) {
        Payroll createdPayroll = payrollService.createPayroll(payroll);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayroll);
    }
    @PostMapping("/create-all")
    public ResponseEntity<List<Payroll>> createPayrollForAllEmployee() {
        List<Payroll> createdPayroll = payrollService.createPayrollForAllEmployee();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayroll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payroll> getPayrollById(@PathVariable Long id) {
        Payroll payroll = payrollService.getPayrollById(id);
        if (payroll != null) {
            return ResponseEntity.ok(payroll);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Payroll>> getAllPayrolls() {
        List<Payroll> payrolls = payrollService.getAllPayrolls();
        return ResponseEntity.ok(payrolls);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payroll> updatePayroll(@PathVariable Long id, @RequestBody Payroll payroll) {
        boolean updated = payrollService.updatePayroll(id, payroll);
        if (updated) {
            return ResponseEntity.ok(payroll);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        boolean deleted = payrollService.deletePayroll(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/createByEmployee")
    public ResponseEntity<Payroll> createPayrollByEmployee(@RequestParam Long employeeId) {
        Payroll payroll = payrollService.createPayrollByEmployee(employeeId);
        return new ResponseEntity<>(payroll, HttpStatus.CREATED);
    }
    @GetMapping("/working-period")
    public List<Payroll> getPayrollsByWorkingPeriod(@RequestParam String workingPeriod) {
        return payrollService.getPayrollsByWorkingPeriod(workingPeriod);
    }
    @GetMapping("/group-by-working-period-status")
    public List<PayrollSummaryResponse> getPayrollGroupByWorkingPeriodAndStatus() {
        return payrollService.getPayrollGroupByWorkingPeriodAndStatus();
    }

}
