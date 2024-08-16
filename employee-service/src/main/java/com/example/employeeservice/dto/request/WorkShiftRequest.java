package com.example.employeeservice.dto.request;

import com.example.employeeservice.entity.Employee;

import java.time.LocalDate;
import java.util.List;

public class WorkShiftRequest {
    Long employeeId;
    List<Long> workShiftId;
    LocalDate workDate;

    public WorkShiftRequest() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<Long> getWorkShiftId() {
        return workShiftId;
    }

    public void setWorkShiftId(List<Long> workShiftId) {
        this.workShiftId = workShiftId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }
}
