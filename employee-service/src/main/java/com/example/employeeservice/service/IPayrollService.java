package com.example.employeeservice.service;

import com.example.employeeservice.entity.Payroll;

import java.util.List;

public interface IPayrollService {

    Payroll createPayroll(Payroll payroll);
    Payroll createPayrollByEmployee(Long employeeId);
    Double calculateMonthlySalary (Long employeeId);

    Payroll getPayrollById(Long id);

    List<Payroll> getAllPayrolls();

    boolean updatePayroll(Long id, Payroll payroll);

    boolean deletePayroll(Long id);
}
