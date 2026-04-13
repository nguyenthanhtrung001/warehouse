package com.example.employeeservice.dto.response;

public class QCLeaderboardDTO {
    private Long employeeId;
    private String employeeName;
    private Long total;

    public QCLeaderboardDTO(Long employeeId, String employeeName, Long total) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.total = total;
    }

    // getters

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}