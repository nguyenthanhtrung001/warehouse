package com.example.employeeservice.controller;

import com.example.employeeservice.entity.WorkShift;
import com.example.employeeservice.service.IWorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshifts")
public class WorkShiftController {

    @Autowired
    private IWorkShiftService workShiftService;


    @PostMapping
    public ResponseEntity<WorkShift> createWorkShift(@RequestBody WorkShift workShift) {
        WorkShift createdWorkShift = workShiftService.createWorkShift(workShift);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkShift);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkShift> getWorkShiftById(@PathVariable Long id) {
        WorkShift workShift = workShiftService.getWorkShiftById(id);
        if (workShift != null) {
            return ResponseEntity.ok(workShift);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<WorkShift>> getAllWorkShifts() {
        List<WorkShift> workShifts = workShiftService.getAllWorkShifts();
        return ResponseEntity.ok(workShifts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkShift> updateWorkShift(@PathVariable Long id, @RequestBody WorkShift workShift) {
        boolean updated = workShiftService.updateWorkShift(id, workShift);
        if (updated) {
            return ResponseEntity.ok(workShift);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkShift(@PathVariable Long id) {
        boolean deleted = workShiftService.deleteWorkShift(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
