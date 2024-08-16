package com.example.goodsservice.controller;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.dto.response.NoteDetailResponse;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.dto.response.ReceiptDetailResponse;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
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
    public ResponseEntity<DeliveryNote> createReceiptWithDetails(@RequestBody Import_Export_Request importExportRequest) {
        try {
            DeliveryNote creatednote = deliveryNoteService.createDeliveryNoteWithDetails(importExportRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(creatednote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/cancel")
    public ResponseEntity<DeliveryNote> createDeliveryNote_Delete_WithDetails(@RequestBody Import_Export_Request importExportRequest) {
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
    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotes() {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNotes();
        return ResponseEntity.ok(deliveryNotes);
    }
    @GetMapping("/cancel")
    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotesCancel() {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNotesCancel();
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
}