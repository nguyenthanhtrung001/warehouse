package com.example.employeeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String namePayroll;

    private  String working_period;
    private int workingDays;

    private  String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNamePayroll() {
        return namePayroll;
    }

    public void setNamePayroll(String namePayroll) {
        this.namePayroll = namePayroll;
    }

    public String getWorking_period() {
        return working_period;
    }

    public void setWorking_period(String working_period) {
        this.working_period = working_period;
    }

    private double bonus;

    @Column(name = "total_income")
    private double totalIncome;

    private double deduction;

    @Column(name = "total_salary")
    private double totalSalary;

    private int status;

    @ManyToOne
    private Employee employee;
    // Constructors, getters, and setters
    public Payroll() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Payroll(int workingDays, double bonus, double totalIncome, double deduction, double totalSalary, int status) {
        this.workingDays = workingDays;
        this.bonus = bonus;
        this.totalIncome = totalIncome;
        this.deduction = deduction;
        this.totalSalary = totalSalary;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}