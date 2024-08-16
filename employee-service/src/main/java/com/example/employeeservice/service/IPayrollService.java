package com.example.employeeservice.service;

import com.example.employeeservice.dto.response.PayrollSummaryResponse;
import com.example.employeeservice.entity.Payroll;

import java.util.List;

public interface IPayrollService {

    Payroll createPayroll(Payroll payroll);
    List<Payroll> createPayrollForAllEmployee();
    Payroll createPayrollByEmployee(Long employeeId);
    Double calculateMonthlySalary (Long employeeId);

    Payroll getPayrollById(Long id);
    Payroll updatePayroll(Payroll payroll);
    List<Payroll> getAllPayrolls();

    boolean updatePayroll(Long id, Payroll payroll);

    boolean deletePayroll(Long id);
    public List<Payroll> getPayrollsByWorkingPeriod(String workingPeriod);
    public List<PayrollSummaryResponse> getPayrollGroupByWorkingPeriodAndStatus();
    public Payroll findPayrollsByWorkingPeriodAndEmployeeId(String workingPeriod, Long employeeId);
}
