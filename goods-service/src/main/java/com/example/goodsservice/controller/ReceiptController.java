package com.example.goodsservice.controller;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.dto.response.ProductSummary;
import com.example.goodsservice.dto.response.ReceiptSummary;
import com.example.goodsservice.dto.response.ReportImportExport;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.service.IReceiptService;
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
    public ResponseEntity<List<Receipt>> getAllReceipts() {
        List<Receipt> receipts = receiptService.getAllReceipts();
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
    public ResponseEntity<Receipt> createReceiptWithDetails(@RequestBody Import_Export_Request importExportRequest) {
        try {
            Receipt createdReceipt = receiptService.createReceiptWithDetails(importExportRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReceipt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/summary")
    public ResponseEntity<ReceiptSummary> getReceiptSummary() {
        ReceiptSummary summary = receiptService.getReceiptSummaryForCurrentMonth();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/report/import-export")
    public List<ReportImportExport> createReportImportExport(
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year) {
        return receiptService.createReportImportExport(month, year);
    }
    @GetMapping("/for-return")
    public ResponseEntity<List<Receipt>> getAllReceiptsForReturn() {
        List<Receipt> receipts = receiptService.getAllReceiptsForReturn();
        return ResponseEntity.ok(receipts);
    }
    @GetMapping("/supplier/{supplierId}")
    public List<ProductSummary> getProductSummaryBySupplierId(@PathVariable Long supplierId) {
        return receiptService.getProductSummaryBySupplierId(supplierId);
    }
}