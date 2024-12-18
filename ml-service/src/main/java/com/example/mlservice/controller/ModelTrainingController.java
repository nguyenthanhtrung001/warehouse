package com.example.mlservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.google.cloud.bigquery.*;

@RestController
@RequestMapping("/api/bigquery")
public class ModelTrainingController {

    @PostMapping("/train")
    public ResponseEntity<String> trainModel() {
        try {
            BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
            String createModelQuery = "CREATE OR REPLACE MODEL `warehouse-439807.warehouse.demand_forecasting_model123` " +
                    "OPTIONS(model_type='ARIMA', time_series_timestamp_col='timestamp', " +
                    "time_series_data_col='demand', time_series_id_col='item_id') AS " +
                    "SELECT timestamp, item_id, demand FROM `warehouse-439807.warehouse.demand_data`";

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(createModelQuery).build();
            bigQuery.query(queryConfig);

            return ResponseEntity.ok("Model trained successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

