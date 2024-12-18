package com.example.mlservice.controller;

import com.example.mlservice.client.ProductClient;
import com.example.mlservice.dto.ForecastItemSummary;
import com.example.mlservice.dto.ForecastResult;
import com.example.mlservice.dto.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.google.cloud.bigquery.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bigquery/forecast")
public class ForecastController {
    @Autowired
    ProductClient productClient;

    @GetMapping("/result")
    public ResponseEntity<?> getForecastResult(@RequestParam Long warehouseId) {
        List<ForecastResult> results = new ArrayList<>();
        Map<String, ForecastItemSummary> itemForecastTotal = new HashMap<>(); // Map để lưu tổng forecast theo itemId
        try {
            BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
            String query = "SELECT * FROM ML.FORECAST(" +
                    "MODEL `warehouse-439807.warehouse.demand_forecasting_model99`, " +
                    "STRUCT(15 AS horizon, 0.8 AS confidence_level))";

            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
            TableResult result = bigQuery.query(queryConfig);

            // Định dạng thời gian từ epoch sang yyyy-MM-dd HH:mm:ss
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

            // Chuyển đổi kết quả thành danh sách ForecastResult
            result.iterateAll().forEach(row -> {
                ForecastResult forecast = new ForecastResult();

                // Chuyển đổi timestamp từ dạng khoa học sang long
                String timestampStr = row.get("forecast_timestamp").getStringValue();
                long timestamp = (long) Double.parseDouble(timestampStr);
                String formattedDate = dateFormatter.format(Instant.ofEpochSecond(timestamp));
                forecast.setForecastTimestamp(formattedDate);

                // Chuyển đổi các giá trị khác và làm tròn
                String itemId = row.get("item_id").getStringValue();
                forecast.setItemId(itemId);

                double forecastValue = row.get("forecast_value").getDoubleValue();
                forecast.setForecastValue(formatDecimal(forecastValue));

                double standardError = row.get("standard_error").getDoubleValue();
                forecast.setStandardError(formatDecimal(standardError));

                double confidenceLevel = row.get("confidence_level").getDoubleValue();
                forecast.setConfidenceLevel(formatDecimal(confidenceLevel));

                double predictionIntervalLowerBound = row.get("prediction_interval_lower_bound").getDoubleValue();
                forecast.setPredictionIntervalLowerBound(formatDecimal(predictionIntervalLowerBound));

                double predictionIntervalUpperBound = row.get("prediction_interval_upper_bound").getDoubleValue();
                forecast.setPredictionIntervalUpperBound(formatDecimal(predictionIntervalUpperBound));

                double confidenceIntervalLowerBound = row.get("confidence_interval_lower_bound").getDoubleValue();
                forecast.setConfidenceIntervalLowerBound(formatDecimal(confidenceIntervalLowerBound));

                double confidenceIntervalUpperBound = row.get("confidence_interval_upper_bound").getDoubleValue();
                forecast.setConfidenceIntervalUpperBound(formatDecimal(confidenceIntervalUpperBound));

                // Thêm vào danh sách kết quả
                results.add(forecast);

                ProductResponse product = getProductNameByItemId(itemId, warehouseId);
                // Lấy tên sản phẩm từ một nguồn dữ liệu (giả sử bạn có dịch vụ hoặc bảng chứa thông tin tên sản phẩm)
                String productName = product.getProductName()!=null?product.getProductName():""; // Hàm lấy tên sản phẩm
                int inventory = product.getQuantity();
                // Cập nhật tổng số forecast cho itemId
                if (itemForecastTotal.containsKey(itemId)) {
                    // Lấy đối tượng ForecastItemSummary đã tồn tại
                    ForecastItemSummary existingSummary = itemForecastTotal.get(itemId);

                    // Làm tròn forecastValue trước khi cộng
                    double roundedForecastValue = Math.round(forecastValue);  // Hoặc sử dụng BigDecimal nếu muốn kiểm soát thêm

                    // Cộng giá trị đã làm tròn vào forecastValue của existingSummary
                    existingSummary.setForecastValue((long) (existingSummary.getForecastValue() + roundedForecastValue));
                } else {
                    // Nếu chưa có itemId trong itemForecastTotal, tạo mới đối tượng ForecastItemSummary
                    double roundedForecastValue = Math.round(forecastValue);  // Hoặc BigDecimal

                    // Thêm đối tượng mới vào Map
                    itemForecastTotal.put(itemId, new ForecastItemSummary(itemId, productName, (long) roundedForecastValue, (long) inventory));
                }

            });

            // Trả về kết quả tổng hợp theo itemId
            return ResponseEntity.ok(Map.of("forecastResults", results, "itemForecastTotal", itemForecastTotal));
        } catch (Exception e) {
            // Trả về thông báo lỗi chi tiết khi có lỗi
            String errorMessage = "Lỗi khi truy vấn dự báo: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Giả sử có một phương thức lấy tên sản phẩm theo itemId
    private ProductResponse getProductNameByItemId(String itemId, Long warehouseId) {
        try {
            ProductResponse product = productClient.getProductByID(Long.valueOf(itemId),warehouseId);
            return  product;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    // Phương thức làm tròn số liệu đến 2 chữ số thập phân
    private String formatDecimal(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .toString();
    }
}
