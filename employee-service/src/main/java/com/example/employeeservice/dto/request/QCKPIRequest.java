package com.example.employeeservice.dto.request;

public class QCKPIRequest {
    private Long employeeId;
    private String date;
    private int targetPerHour;
    private int workingHours;

    public QCKPIRequest() {
    }

    public QCKPIRequest(Long employeeId, String date, int targetPerHour, int workingHours) {
        this.employeeId = employeeId;
        this.date = date;
        this.targetPerHour = targetPerHour;
        this.workingHours = workingHours;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTargetPerHour() {
        return targetPerHour;
    }

    public void setTargetPerHour(int targetPerHour) {
        this.targetPerHour = targetPerHour;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }
}