package com.example.employeeservice.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_shift_id")
    private WorkShift workShift;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "work_date")
    private LocalDate workDate;

    private int status;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "late_time")
    private Long lateTime;
    @Column(name = "early_time")
    private Long earlyTime;

    public Long getEarlyTime() {
        return earlyTime;
    }

    public void setEarlyTime(Long earlyTime) {
        this.earlyTime = earlyTime;
    }

    public Long getLateTime() {
        return lateTime;
    }

    public void setLateTime(Long lastTimeInSeconds) {
        this.lateTime = lastTimeInSeconds;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    private String note;

    // Constructors, getters, and setters
    public Attendance() {
    }

    public Attendance(WorkShift workShift, Employee employee, LocalDate workDate, int status, String note) {
        this.workShift = workShift;
        this.employee = employee;
        this.workDate = workDate;
        this.status = status;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkShift getWorkShift() {
        return workShift;
    }

    public void setWorkShift(WorkShift workShift) {
        this.workShift = workShift;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}