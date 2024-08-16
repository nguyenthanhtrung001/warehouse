package com.example.inventoryservice.controller;


import com.example.inventoryservice.dto.InventoryCheckSlipRequest;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.InventoryCheckDetail;
import com.example.inventoryservice.entity.InventoryCheckSlip;
import com.example.inventoryservice.service.IInventoryCheckSlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-check-slips")
public class InventoryCheckSlipController {

    @Autowired
    private IInventoryCheckSlipService inventoryCheckSlipService;


    @PostMapping
    public ResponseEntity<InventoryCheckSlip> createInventoryCheckSlip(@RequestBody InventoryCheckSlipRequest inventoryCheckSlipRequest) {
        try {
            InventoryCheckSlip createdSlip = inventoryCheckSlipService.createInventoryCheckSlip(inventoryCheckSlipRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSlip);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryCheckSlip> getInventoryCheckSlipById(@PathVariable Long id) {
        InventoryCheckSlip inventoryCheckSlip = inventoryCheckSlipService.getInventoryCheckSlipById(id);
        if (inventoryCheckSlip != null) {
            return ResponseEntity.ok(inventoryCheckSlip);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<InventoryCheckSlip>> getAllInventoryCheckSlips() {
        List<InventoryCheckSlip> inventoryCheckSlips = inventoryCheckSlipService.getAllInventoryCheckSlips();
        return ResponseEntity.ok(inventoryCheckSlips);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryCheckSlip> updateInventoryCheckSlip(@PathVariable Long id, @RequestBody InventoryCheckSlipRequest inventoryCheckSlipRequest) {
        try {
            InventoryCheckSlip updatedSlip = inventoryCheckSlipService.updateInventoryCheckSlip(id, inventoryCheckSlipRequest);
            if (updatedSlip != null) {
                return ResponseEntity.ok(updatedSlip);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryCheckSlip(@PathVariable Long id) {
        boolean deleted = inventoryCheckSlipService.deleteInventoryCheckSlip(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/details/{id}")
    public List<InventoryCheckDetail> getDetailCheckSlipById(@PathVariable Long id) {
        return inventoryCheckSlipService.getDetailCheckSlipById(id);
    }
    @GetMapping("/products/discrepancies/current-month")
    public List<ProductQuantity> getProductDiscrepanciesForCurrentMonth() {
        return inventoryCheckSlipService.getProductDiscrepanciesForCurrentMonth();
    }

    @GetMapping("/products/discrepancies/by-month-year")
    public List<ProductQuantity> getProductDiscrepanciesForMonthYear(@RequestParam int month,
                                                                        @RequestParam int year) {
        return inventoryCheckSlipService.getProductDiscrepanciesForMonthYear(month, year);
    }

    @GetMapping("/products/discrepancies-less/current-month")
    public List<ProductQuantity> getProductDiscrepanciesLessForCurrentMonth() {
        return inventoryCheckSlipService.getProductDiscrepanciesLessForCurrentMonth();
    }

    @GetMapping("/products/discrepancies-less/by-month-year")
    public List<ProductQuantity> getProductDiscrepanciesLessForMonthYear(@RequestParam int month,
                                                                     @RequestParam int year) {
        return inventoryCheckSlipService.getProductDiscrepanciesLessForMonthYear(month, year);
    }
}

