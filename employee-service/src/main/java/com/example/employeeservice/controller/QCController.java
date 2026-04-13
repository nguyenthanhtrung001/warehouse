package com.example.employeeservice.controller;

import com.example.employeeservice.dto.request.QCKPIRequest;
import com.example.employeeservice.dto.request.QCRequest;
import com.example.employeeservice.dto.response.ApiResponse;
import com.example.employeeservice.dto.response.QCDashboardResponse;
import com.example.employeeservice.dto.response.QCLeaderboardDTO;
import com.example.employeeservice.entity.QC;
import com.example.employeeservice.entity.QCKPIConfig;
import com.example.employeeservice.service.IQCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/qc")
public class QCController {

    private final IQCService qcService;

    public QCController(IQCService qcService) {
        this.qcService = qcService;
    }

    // ✅ SCAN
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
    @PostMapping("/scan")
    public ResponseEntity<ApiResponse<QC>> scan(@RequestBody QCRequest request) {
        QC qc = qcService.scan(request);

        return ResponseEntity.ok(
                ApiResponse.<QC>builder()
                        .message("Scan QC success")
                        .result(qc)
                        .build()
        );
    }

    // ✅ DASHBOARD
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<QCDashboardResponse>> dashboard(
            @RequestParam Long employeeId,
            @RequestParam(required = false) String date
    ) {
        LocalDate d = (date == null) ? LocalDate.now() : LocalDate.parse(date);

        return ResponseEntity.ok(
                ApiResponse.<QCDashboardResponse>builder()
                        .message("Get dashboard success")
                        .result(qcService.getDashboard(employeeId, d))
                        .build()
        );
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<QC>>> search(
            @RequestParam Long employeeId,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(
                ApiResponse.<List<QC>>builder()
                        .message("Search success")
                        .result(qcService.search(employeeId, keyword))
                        .build()
        );
    }

    // ✅ DELETE
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @RequestParam Long employeeId
    ) {
        qcService.delete(id, employeeId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Delete QC success")
                        .build()
        );
    }

    // ✅ INIT
    @PostMapping("/init")
    public ResponseEntity<ApiResponse<Void>> init(
            @RequestParam Long employeeId,
            @RequestParam int value
    ) {
        qcService.init(employeeId, value);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Init QC success")
                        .build()
        );
    }

    // ✅ SET KPI
    @PostMapping("/kpi-config")
    public ResponseEntity<ApiResponse<Void>> setKPI(@RequestBody QCKPIRequest request) {
        qcService.setKPI(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Set KPI success")
                        .build()
        );
    }

    // ✅ GET KPI
    @GetMapping("/kpi-config")
    public ResponseEntity<ApiResponse<QCKPIConfig>> getKPI(
            @RequestParam Long employeeId,
            @RequestParam String date
    ) {
        return ResponseEntity.ok(
                ApiResponse.<QCKPIConfig>builder()
                        .message("Get KPI success")
                        .result(qcService.getKPI(employeeId, LocalDate.parse(date)))
                        .build()
        );
    }
    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse<List<QCLeaderboardDTO>>> leaderboard(
            @RequestParam String date
    ) {
        return ResponseEntity.ok(
                ApiResponse.<List<QCLeaderboardDTO>>builder()
                        .message("Get leaderboard success")
                        .result(qcService.getLeaderboard(LocalDate.parse(date)))
                        .build()
        );
    }
}