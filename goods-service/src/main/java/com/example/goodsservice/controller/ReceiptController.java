package com.example.goodsservice.controller;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.dto.response.*;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.service.IReceiptService;
import com.example.goodsservice.validation.ImportExportRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @Autowired
    private IReceiptService receiptService;

    @GetMapping("/{id}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable Long id) {
        Receipt receipt = receiptService.getReceiptById(id);
        if (receipt != null) {
            return ResponseEntity.ok(receipt);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Receipt>> getAllReceipts(@RequestParam("warehouseId") Long warehouseId) {
        List<Receipt> receipts = receiptService.getAllReceipts(warehouseId);
        return ResponseEntity.ok(receipts);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReceipt(@PathVariable Long id, @RequestBody Receipt receipt) {
        boolean updated = receiptService.updateReceipt(id, receipt);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long id) {
        boolean deleted = receiptService.deleteReceiptUpdateStatus(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateReceiptStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean updated = receiptService.updateReceiptStatus(id, status);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{receiptId}/delivery-notes")
    public ResponseEntity<List<DeliveryNote>> getDeliveryNotesByReceiptId(@PathVariable Long receiptId) {
        List<DeliveryNote> deliveryNotes = receiptService.getDeliveryNotesByReceiptId(receiptId);
        return ResponseEntity.ok(deliveryNotes);
    }

    @PostMapping
    public ResponseEntity<?> createReceiptWithDetails(@RequestBody Import_Export_Request importExportRequest) {
        try {
            // Kiểm tra dữ liệu đầu vào
            List<String> validationErrors = ImportExportRequestValidator.validateRequest(importExportRequest);
            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Dữ liệu không hợp lệ", validationErrors));
            }

            // Tạo Receipt với chi tiết
            Receipt createdReceipt = receiptService.createReceiptWithDetails(importExportRequest);

            // Trả về thành công
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Phiếu nhập được tạo thành công", createdReceipt));

        } catch (IllegalArgumentException e) {
            // Trả về lỗi với thông báo chi tiết
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));

        } catch (Exception e) {
            // Ghi log lỗi và trả về phản hồi lỗi hệ thống
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Đã xảy ra lỗi không xác định", null));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Receipt> createImportTransfer(@RequestBody Import_Export_Request importExportRequest) {
        try {
            Receipt createdReceipt = receiptService.createImportTransfer(importExportRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReceipt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/straightforwardness")
    public ResponseEntity<ReceiptSummary> getReceiptSummary(@RequestParam("warehouseId") Long warehouseId) {
        ReceiptSummary summary = receiptService.getReceiptSummaryForCurrentMonth(warehouseId);
        return ResponseEntity.ok(summary);
    }
    @GetMapping("/summary")
    public ResponseEntity<ReceiptSummary> getReceiptSummary() {
        ReceiptSummary summary = receiptService.getReceiptSummaryForCurrentMonth();
        return ResponseEntity.ok(summary);
    }



    @GetMapping("/report/import-export")
    public List<ReportImportExport> createReportImportExport(
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year,
            @RequestParam("warehouseId") Long warehouseId) {
        return receiptService.createReportImportExport(month, year, warehouseId);
    }

    @GetMapping("/for-return")
    public ResponseEntity<List<Receipt>> getAllReceiptsForReturn(@RequestParam Long warehouseId) {
        List<Receipt> receipts = receiptService.getAllReceiptsForReturn(warehouseId);
        return ResponseEntity.ok(receipts);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<ProductSummary> getProductSummaryBySupplierId(
            @PathVariable Long supplierId,
            @RequestParam Long warehouseId,
            @RequestParam int year,
            @RequestParam int month) {

        return receiptService.getProductSummaryBySupplierId(supplierId, warehouseId, year, month);
    }


}