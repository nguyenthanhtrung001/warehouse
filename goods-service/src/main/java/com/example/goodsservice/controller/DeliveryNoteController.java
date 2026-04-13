package com.example.goodsservice.controller;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.dto.response.ApiResponse;
import com.example.goodsservice.dto.response.DeliverySummaryResponse;
import com.example.goodsservice.dto.response.NoteDetailResponse;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.service.IDeliveryDetailService;
import com.example.goodsservice.service.IDeliveryNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveryNotes")
public class DeliveryNoteController {

    @Autowired
    private IDeliveryNoteService deliveryNoteService;
    @Autowired
    private IDeliveryDetailService deliveryDetailService;

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryNote>> createDeliveryNoteWithDetails(@RequestBody Import_Export_Request importExportRequest) {
        try {
            DeliveryNote transfer = deliveryNoteService.createDeliveryNoteWithDetails(importExportRequest);
            ApiResponse<DeliveryNote> response = new ApiResponse<>(true, "Phiếu xuất tạo thành công", transfer);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse<DeliveryNote> response = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<DeliveryNote> response = new ApiResponse<>(false, "Internal server error.", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<DeliveryNote>> createTransfer(@RequestBody Import_Export_Request importExportRequest) {
        try {
            DeliveryNote transfer = deliveryNoteService.createTransfer(importExportRequest);
            ApiResponse<DeliveryNote> response = new ApiResponse<>(true, "Phiếu chuyển kho tạo thành công", transfer);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse<DeliveryNote> response = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<DeliveryNote> response = new ApiResponse<>(false, "Internal server error.", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/cancel")
    public ResponseEntity<DeliveryNote> createDeliveryNote_Cancel_WithDetails(@RequestBody Import_Export_Request importExportRequest) {
        try {
            DeliveryNote creatednote = deliveryNoteService.createDeliveryNote_Delete_WithDetails(importExportRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(creatednote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryNote> getDeliveryNoteById(@PathVariable Long id) {
        DeliveryNote deliveryNote = deliveryNoteService.getDeliveryNoteById(id);
        if (deliveryNote != null) {
            return ResponseEntity.ok(deliveryNote);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotes(@RequestParam Long warehouseId) {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNotes(warehouseId);
        return ResponseEntity.ok(deliveryNotes);
    }

    @GetMapping("/cancel")
    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotesCancel(@RequestParam Long warehouseId) {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNotesCancel(warehouseId);
        return ResponseEntity.ok(deliveryNotes);
    }
    @GetMapping("/transfer")
    public ResponseEntity<List<DeliveryNote>> getAllTransfer(@RequestParam Long warehouseId) {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllTransfer(warehouseId);
        return ResponseEntity.ok(deliveryNotes);
    }
    @GetMapping("/import-transfer")
    public ResponseEntity<List<DeliveryNote>> getAllImportTransfer(@RequestParam Long warehouseId) {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllImportTransfer(warehouseId);
        return ResponseEntity.ok(deliveryNotes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryNote> updateDeliveryNote(@PathVariable Long id, @RequestBody DeliveryNote deliveryNoteDetails) {
        boolean updated = deliveryNoteService.updateDeliveryNote(id, deliveryNoteDetails);
        if (updated) {
            return ResponseEntity.ok(deliveryNoteDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateDeliveryNoteStatus(@PathVariable Long id, @RequestBody Integer status) {
        boolean updated = deliveryNoteService.updateDeliveryNoteStatus(id, status);
        if (updated) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryNote(@PathVariable Long id) {
        boolean deleted = deliveryNoteService.deleteDeliveryNote(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("cancel-transfer/{id}")
    public ResponseEntity<Void> cancelTransfer(@PathVariable Long id, @RequestBody String reason ){
        boolean cancelTransfer = deliveryNoteService.cancelTransfer(id, reason);
        if (cancelTransfer) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("complete-transfer/{id}")
    public ResponseEntity<ApiResponse<String>> completeTransfer(@PathVariable Long id) {
        boolean isUpdated = deliveryNoteService.updateDeliveryNoteStatus(id, 3);
        if (isUpdated) {
            ApiResponse<String> response = new ApiResponse<>(true, "Chuyển kho đã được hoàn tất.", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Không tìm thấy phiếu chuyển kho với ID: " + id, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/{noteId}/details")
    public ResponseEntity<List<NoteDetailResponse>> getNoteDetails(@PathVariable Long noteId) {
        List<NoteDetailResponse> noteDetails = deliveryDetailService.getNoteDetails(noteId);
        return ResponseEntity.ok(noteDetails);
    }
    @GetMapping("/products/quantities/current-month/type")
    public List<ProductQuantity> getProductQuantitiesForCurrentMonthAndType(@RequestParam int type) {
        return deliveryDetailService.getProductQuantitiesForCurrentMonthAndType(type);
    }

    @GetMapping("/products/quantities/by-month-year/type")
    public List<ProductQuantity> getProductQuantitiesForMonthYearAndType(@RequestParam int month,
                                                                         @RequestParam int year,
                                                                         @RequestParam int type) {
        return deliveryDetailService.getProductQuantitiesForMonthYearAndType(month, year, type);
    }
    @GetMapping("/total-quantity")
    public ResponseEntity<Integer> getTotalQuantity(
            @RequestParam Long receiptId,
            @RequestParam Long batchDetailId) {

        Integer totalQuantity = deliveryDetailService.getTotalQuantity(receiptId, batchDetailId);

        if (totalQuantity != null) {
            return ResponseEntity.ok(totalQuantity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/delivery-summary")
    public ApiResponse<DeliverySummaryResponse> getDeliverySummary(
            @RequestParam Integer type,
            @RequestParam Integer status) {

        // Gọi service để lấy tổng số phiếu và tổng số lượng
        DeliverySummaryResponse summary = deliveryNoteService.getSummaryByTypeAndStatus(type, status);

        // Trả về kết quả với ApiResponse
        return new ApiResponse<>(true, "Summary retrieved successfully", summary);
    }

}