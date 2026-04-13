package com.example.employeeservice.dto.response;

import com.example.employeeservice.entity.QC;

import java.util.List;

public class QCDashboardResponse {
    private long total;
    private double avgSpeed;
    private int kpiPercent;
    private List<QC> items;

    public QCDashboardResponse(long total, double avgSpeed, int kpiPercent, List<QC> items) {
        this.total = total;
        this.avgSpeed = avgSpeed;
        this.kpiPercent = kpiPercent;
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getKpiPercent() {
        return kpiPercent;
    }

    public void setKpiPercent(int kpiPercent) {
        this.kpiPercent = kpiPercent;
    }

    public List<QC> getItems() {
        return items;
    }

    public void setItems(List<QC> items) {
        this.items = items;
    }
}