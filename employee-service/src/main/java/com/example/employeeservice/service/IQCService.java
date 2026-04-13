package com.example.employeeservice.service;

import com.example.employeeservice.dto.request.QCKPIRequest;
import com.example.employeeservice.dto.request.QCRequest;
import com.example.employeeservice.dto.response.QCDashboardResponse;
import com.example.employeeservice.dto.response.QCLeaderboardDTO;
import com.example.employeeservice.entity.QC;
import com.example.employeeservice.entity.QCKPIConfig;

import java.time.LocalDate;
import java.util.List;

public interface IQCService {

    QC scan(QCRequest request);

    QCDashboardResponse getDashboard(Long employeeId, LocalDate date);

    List<QC> search(Long employeeId, String keyword);

    void delete(Long id, Long employeeId);

    void init(Long employeeId, int value);

    void setKPI(QCKPIRequest request);

    QCKPIConfig getKPI(Long employeeId, LocalDate date);
    //qc
    List<QCLeaderboardDTO> getLeaderboard(LocalDate date);

}