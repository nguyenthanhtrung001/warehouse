package com.example.employeeservice.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "qc_kpi_config")
public class QCKPIConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private LocalDate date;

    private int targetPerHour;

    private int workingHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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