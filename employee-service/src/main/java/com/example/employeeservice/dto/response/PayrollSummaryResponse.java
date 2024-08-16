package com.example.employeeservice.dto.response;

public class PayrollSummaryResponse {
    private String namePayroll;
    private String workingPeriod;
    private int status;
    private double totalSalarySum;
    private Long count;

    public PayrollSummaryResponse() {
    }

    public PayrollSummaryResponse(String namePayroll, String workingPeriod, int status, double totalSalarySum, Long count) {
        this.namePayroll = namePayroll;
        this.workingPeriod = workingPeriod;
        this.status = status;
        this.totalSalarySum = totalSalarySum;
        this.count = count;
    }

    public String getNamePayroll() {
        return namePayroll;
    }

    public void setNamePayroll(String namePayroll) {
        this.namePayroll = namePayroll;
    }

    public String getWorkingPeriod() {
        return workingPeriod;
    }

    public void setWorkingPeriod(String workingPeriod) {
        this.workingPeriod = workingPeriod;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotalSalarySum() {
        return totalSalarySum;
    }

    public void setTotalSalarySum(double totalSalarySum) {
        this.totalSalarySum = totalSalarySum;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
