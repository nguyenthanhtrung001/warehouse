package com.example.mlservice.controller;

import com.example.mlservice.dto.ForecastResult;
import com.example.mlservice.service.IBigQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/bigquery")
public class BigQueryController {

    @Autowired
    private IBigQueryService bigQueryService;

    // API để kiểm tra kết nối
    @GetMapping("/test-connection")
    public ResponseEntity<String> testConnection() {
        String message = bigQueryService.testConnection();
        return ResponseEntity.ok(message);
    }

//    // API để tải lên file CSV
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file,
//                                                @RequestParam("datasetName") String datasetName,
//                                                @RequestParam("tableName") String tableName) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("File is empty!");
//        }
//
//        String responseMessage = bigQueryService.uploadCsvFile(file, datasetName, tableName);
//        return ResponseEntity.ok(responseMessage);
//    }

    @GetMapping("/forecast")
    public ResponseEntity<List<ForecastResult>> getForecastResults() {
        List<ForecastResult> results = bigQueryService.getForecastResults();
        return ResponseEntity.ok(results);
    }
}
