package com.example.orderservice.controller;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.service.IInvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/export")
public class CsvExportController {

    @Autowired
    private IInvoiceDetailService salesService;

    @GetMapping("/sales")
    public ApiResponse<String> exportSalesToCsv() {
        // Đường dẫn file CSV (có thể điều chỉnh)
        String filePath = "sales.csv";
        try {
            // Gọi service để xuất dữ liệu ra file CSV
            String resultMessage = salesService.exportSalesToCsv(filePath);

            // Trả về đối tượng ApiResponse với thông báo thành công
            return new ApiResponse<>(true, "Sales data exported successfully!", resultMessage);
        } catch (Exception e) {
            // Trả về đối tượng ApiResponse với thông báo lỗi nếu có ngoại lệ
            return new ApiResponse<>(false, "Error exporting sales data: " + e.getMessage(), null);
        }
    }
}
