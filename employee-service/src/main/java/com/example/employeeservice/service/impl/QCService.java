package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.request.QCKPIRequest;
import com.example.employeeservice.dto.request.QCRequest;
import com.example.employeeservice.dto.response.QCDashboardResponse;
import com.example.employeeservice.dto.response.QCLeaderboardDTO;
import com.example.employeeservice.entity.QC;
import com.example.employeeservice.entity.QCKPIConfig;
import com.example.employeeservice.repository.QCKPIRepository;
import com.example.employeeservice.repository.QCRepository;
import com.example.employeeservice.service.IQCService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QCService implements IQCService {

    private final QCRepository qcRepo;
    private final QCKPIRepository kpiRepo;

    public QCService(QCRepository qcRepo, QCKPIRepository kpiRepo) {
        this.qcRepo = qcRepo;
        this.kpiRepo = kpiRepo;
    }

    @Override
    public QC scan(QCRequest request) {
        if (qcRepo.existsByQcCode(request.getQcCode())) {
            throw new RuntimeException("QC exists");
        }

        QC qc = new QC();
        qc.setQcCode(request.getQcCode());
        qc.setScanTime(LocalDateTime.now());
        qc.setType(request.getType());
        qc.setProductName(request.getProductName());
        qc.setEmployeeId(request.getEmployeeId());

        return qcRepo.save(qc);
    }

    @Override
    public QCDashboardResponse getDashboard(Long employeeId, LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<QC> list = qcRepo.findByEmployeeAndTimeRange(employeeId, start, end);

        long total = list.size();

        double avgSpeed = 0;
        if (total > 1) {
            long seconds = Duration.between(
                    list.get(list.size() - 1).getScanTime(),
                    list.get(0).getScanTime()
            ).getSeconds();
            avgSpeed = seconds / (double) total;
        }

        int kpiTarget = 800;

        var config = kpiRepo.findByEmployeeIdAndDate(employeeId, date);
        if (config.isPresent()) {
            kpiTarget = config.get().getTargetPerHour() * config.get().getWorkingHours();
        }

        int percent = (int) ((total * 100.0) / kpiTarget);

        return new QCDashboardResponse(total, avgSpeed, percent, list);
    }

    @Override
    public List<QC> search(Long employeeId, String keyword) {
        return qcRepo.findByEmployeeIdAndDeletedFalseAndQcCodeContainingIgnoreCase(employeeId, keyword);
    }

    @Override
    public void delete(Long id, Long employeeId) {
        QC qc = qcRepo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!qc.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("Not allowed");
        }

        qc.setDeleted(true);
        qcRepo.save(qc);
    }

    @Override
    public void init(Long employeeId, int value) {
        for (int i = 0; i < value; i++) {
            QC qc = new QC();
            qc.setQcCode("INIT_" + System.nanoTime());
            qc.setScanTime(LocalDateTime.now());
            qc.setEmployeeId(employeeId);
            qcRepo.save(qc);
        }
    }

    @Override
    public void setKPI(QCKPIRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());

        QCKPIConfig config = kpiRepo
                .findByEmployeeIdAndDate(request.getEmployeeId(), date)
                .orElse(new QCKPIConfig());

        config.setEmployeeId(request.getEmployeeId());
        config.setDate(date);
        config.setTargetPerHour(request.getTargetPerHour());
        config.setWorkingHours(request.getWorkingHours());

        kpiRepo.save(config);
    }

    @Override
    public QCKPIConfig getKPI(Long employeeId, LocalDate date) {
        return kpiRepo.findByEmployeeIdAndDate(employeeId, date).orElse(null);
    }
    @Override
    public List<QCLeaderboardDTO> getLeaderboard(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return qcRepo.getLeaderboard(start, end);
    }
}