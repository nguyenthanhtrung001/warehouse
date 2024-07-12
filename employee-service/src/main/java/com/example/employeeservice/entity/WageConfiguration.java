package com.example.employeeservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class WageConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latePenalty; // Phạt nếu trễ

    private Double bonus; // Thưởng thêm

    private Double earlyPenalty; // Tiền bị về sớm

    private  Integer Hours;

    // Constructors, getters, setters
    public WageConfiguration() {
    }

    public Integer getHours() {
        return Hours;
    }

    public void setHours(Integer hours) {
        Hours = hours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Double getLatePenalty() {
        return latePenalty;
    }

    public void setLatePenalty(Double latePenalty) {
        this.latePenalty = latePenalty;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getEarlyPenalty() {
        return earlyPenalty;
    }

    public void setEarlyPenalty(Double leaveDeduction) {
        this.earlyPenalty = leaveDeduction;
    }
}
