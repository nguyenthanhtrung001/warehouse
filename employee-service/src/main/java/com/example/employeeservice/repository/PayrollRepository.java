package com.example.employeeservice.repository;

import com.example.employeeservice.dto.response.PayrollSummaryResponse;
import com.example.employeeservice.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    @Query("SELECT new com.example.employeeservice.dto.response.PayrollSummaryResponse(p.namePayroll, p.working_period, p.status, SUM(p.totalSalary), COUNT(p)) " +
            "FROM Payroll p GROUP BY p.namePayroll, p.working_period")
    List<PayrollSummaryResponse> findPayrollGroupByWorkingPeriodAndStatus();

    @Query("SELECT p FROM Payroll p WHERE p.working_period = ?1")
    List<Payroll> findByWorkingPeriod(String workingPeriod);

    @Query("SELECT p FROM Payroll p WHERE p.working_period = :workingPeriod AND p.employee.id = :employeeId")
    Payroll findByWorkingPeriodAndEmployeeId(@Param("workingPeriod") String workingPeriod, @Param("employeeId") Long employeeId);
}
