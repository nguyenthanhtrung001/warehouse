package com.example.mlservice.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.google.cloud.bigquery.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api/bigquery/csv")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("filePath") String filePath) {
        // Kiểm tra xem file có tồn tại không
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File does not exist at the specified path: " + filePath);
        }

        List<Map<String, Object>> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Bỏ qua dòng tiêu đề nếu có

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // Sử dụng dấu phẩy làm ký tự phân tách

                // Kiểm tra số lượng giá trị
                if (values.length < 3) {
                    continue; // Bỏ qua dòng không hợp lệ
                }

                Map<String, Object> row = new HashMap<>();

                // Xử lý giá trị timestamp
                String timestamp = values[0];
                if (timestamp.length() == 16) {
                    timestamp += ":00"; // Thêm giây nếu thiếu
                }

                row.put("timestamp", timestamp.replace("T", " ")); // Chuyển sang định dạng "yyyy-MM-dd HH:mm:ss"
                row.put("item_id", values[1]); // item_id
                row.put("demand", Integer.parseInt(values[2])); // demand

                rows.add(row);
            }

            // Tải dữ liệu lên BigQuery
            uploadToBigQuery(rows);
            return ResponseEntity.ok("File data processed and saved to BigQuery successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    private void uploadToBigQuery(List<Map<String, Object>> rows) throws Exception {
        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
        String datasetName = "warehouse";
        String tableName = "demand_data";
        TableId tableId = TableId.of(datasetName, tableName);

        // Kiểm tra bảng đã tồn tại chưa
        Table table = bigQuery.getTable(tableId);
        if (table == null) {
            // Tạo bảng nếu chưa tồn tại
            Schema schema = Schema.of(
                    Field.of("timestamp", LegacySQLTypeName.DATETIME),
                    Field.of("item_id", LegacySQLTypeName.STRING),
                    Field.of("demand", LegacySQLTypeName.INTEGER)
            );
            TableDefinition tableDef = StandardTableDefinition.of(schema);
            TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDef).build();

            // Tạo dataset nếu chưa tồn tại
            DatasetId datasetId = DatasetId.of(datasetName);
            if (bigQuery.getDataset(datasetId) == null) {
                DatasetInfo datasetInfo = DatasetInfo.newBuilder(datasetId).build();
                bigQuery.create(datasetInfo);
            }

            // Tạo bảng mới
            bigQuery.create(tableInfo);
        }

        // Chèn dữ liệu vào bảng
        List<InsertAllRequest.RowToInsert> bigQueryRows = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            bigQueryRows.add(InsertAllRequest.RowToInsert.of(row));
        }

        InsertAllResponse response = bigQuery.insertAll(
                InsertAllRequest.newBuilder(tableId, bigQueryRows).build()
        );

        if (response.hasErrors()) {
            response.getInsertErrors().forEach((index, errors) -> {
                System.err.println("Error inserting row " + index + ": " + errors);
            });
            throw new Exception("Error inserting data into BigQuery");
        }
    }
}
