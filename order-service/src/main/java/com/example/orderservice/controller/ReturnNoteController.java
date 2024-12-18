package com.example.orderservice.controller;
import com.example.orderservice.dto.ReturnNoteRequest;
import com.example.orderservice.dto.response.MonthRevenue;
import com.example.orderservice.entity.ReturnNote;

import com.example.orderservice.service.IReturnNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/return-notes")
public class ReturnNoteController {
    @Autowired
    private IReturnNoteService returnNoteService;

    @PostMapping
    public ResponseEntity<ReturnNote> createReturnNoteAndDetails(@RequestBody ReturnNoteRequest returnNoteRequest) {
        ReturnNote returnNote = returnNoteService.createReturnNoteAndDetails(returnNoteRequest);
        return ResponseEntity.ok(returnNote);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnNote> getReturnNoteById(@PathVariable Long id) {
        ReturnNote returnNote = returnNoteService.getReturnNoteById(id);
        if (returnNote != null) {
            return ResponseEntity.ok(returnNote);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ReturnNote>> getAllReturnNotes(@PathVariable Long warehouseId) {
        List<ReturnNote> returnNotes = returnNoteService.getAllReturnNotes(warehouseId);
        return ResponseEntity.ok(returnNotes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnNote> updateReturnNote(@PathVariable Long id, @RequestBody ReturnNote returnNote) {
        boolean updated = returnNoteService.updateReturnNote(id, returnNote);
        if (updated) {
            return ResponseEntity.ok(returnNote);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnNote(@PathVariable Long id) {
        boolean deleted = returnNoteService.deleteReturnNote(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/revenue/monthly")
    public long getMonthlyRevenue(@RequestParam("warehouseId") Long warehouseId) {
        return returnNoteService.calculateRevenueForCurrentMonth(warehouseId);
    }
    @GetMapping("/revenue-warehouse/monthly")
    public long getMonthlyRevenuerHouse() {
        return returnNoteService.calculateRevenueForCurrentMonth();
    }
    @GetMapping("/revenue-warehouse-12-month")
    public List<MonthRevenue> getRevenueNMonth() {
        return returnNoteService.getRevenueNMonth();
    }
    @GetMapping("/count/current-month")
    public long getReturnNoteCountForCurrentMonth(@RequestParam Long warehouseId) {
        return returnNoteService.countReturnNotesForCurrentMonth(warehouseId);
    }

}
