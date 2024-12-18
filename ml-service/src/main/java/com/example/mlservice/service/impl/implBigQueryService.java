package com.example.mlservice.service.impl;

import com.example.mlservice.dto.ForecastResult;
import com.example.mlservice.service.IBigQueryService;
import com.google.cloud.bigquery.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class implBigQueryService implements IBigQueryService {
    private final BigQuery bigQuery;

    // Sử dụng @Value để lấy Project ID từ tệp cấu hình
    public implBigQueryService(@Value("${google.cloud.project-id}") String projectId) {
        this.bigQuery = BigQueryOptions.newBuilder().setProjectId(projectId).build().getService();
    }

    // Các phương thức để tương tác với BigQuery
    public String testConnection() {
        List<String> result = new ArrayList<>(); // Sử dụng danh sách để lưu kết quả
        try {
            // Truy vấn đơn giản để kiểm tra kết nối
            String query = "SELECT 'Kết nối thành công với BigQuery!' AS message";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
            TableResult tableResult = bigQuery.query(queryConfig);

            // Lặp qua các dòng kết quả
            tableResult.iterateAll().forEach(row -> result.add(row.get("message").getStringValue()));
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return result.isEmpty() ? "No result" : result.get(0); // Trả về kết quả đầu tiên
    }
    @Override
    public List<ForecastResult> getForecastResults() {
        List<ForecastResult> results = new ArrayList<>();
        try {
            // Truy vấn để lấy kết quả dự đoán từ mô hình
            String query = "SELECT * FROM ML.FORECAST(" +
                    "MODEL `warehouse-439807.warehouse.demand_forecasting_model`, " +
                    "STRUCT(30 AS horizon, 0.8 AS confidence_level))";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
            TableResult tableResult = bigQuery.query(queryConfig);

            // Lặp qua các dòng kết quả và thêm vào danh sách
            for (FieldValueList row : tableResult.iterateAll()) {
                ForecastResult forecast = new ForecastResult();
                forecast.setItemId(row.get("item_id").getStringValue());
                forecast.setForecastTimestamp(row.get("forecast_timestamp").getStringValue());
                forecast.setForecastValue(row.get("forecast_value").getStringValue());
                forecast.setStandardError(row.get("standard_error").getStringValue());
                forecast.setConfidenceLevel(row.get("confidence_level").getStringValue());
                forecast.setPredictionIntervalLowerBound(row.get("prediction_interval_lower_bound").getStringValue());
                forecast.setPredictionIntervalUpperBound(row.get("prediction_interval_upper_bound").getStringValue());
                forecast.setConfidenceIntervalLowerBound(row.get("confidence_interval_lower_bound").getStringValue());
                forecast.setConfidenceIntervalUpperBound(row.get("confidence_interval_upper_bound").getStringValue());
                results.add(forecast);
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi cho việc xử lý
        }
        return results.isEmpty() ? List.of() : results;
    }
}



