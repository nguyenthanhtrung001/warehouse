package com.example.orderservice.controller;
import com.example.orderservice.dto.ReturnNoteRequest;
import com.example.orderservice.entity.ReturnNote;

import com.example.orderservice.service.IReturnNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<List<ReturnNote>> getAllReturnNotes() {
        List<ReturnNote> returnNotes = returnNoteService.getAllReturnNotes();
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
    public long getMonthlyRevenue() {
        return returnNoteService.calculateRevenueForCurrentMonth();
    }
    @GetMapping("/count/current-month")
    public long getReturnNoteCountForCurrentMonth() {
        return returnNoteService.countReturnNotesForCurrentMonth();
    }
}
