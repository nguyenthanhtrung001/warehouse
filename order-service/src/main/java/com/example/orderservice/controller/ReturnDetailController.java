package com.example.orderservice.controller;

import com.example.orderservice.dto.response.InvoiceDetailResponse;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.ReturnDetail;
import com.example.orderservice.service.IReturnDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/return-details")
public class ReturnDetailController {
    @Autowired
    private IReturnDetailService returnDetailService;

       @PostMapping
    public ResponseEntity<ReturnDetail> createReturnDetail(@RequestBody ReturnDetail returnDetail) {
        ReturnDetail createdReturnDetail = returnDetailService.createReturnDetail(returnDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReturnDetail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnDetail> getReturnDetailById(@PathVariable Long id) {
        ReturnDetail returnDetail = returnDetailService.getReturnDetailById(id);
        if (returnDetail != null) {
            return ResponseEntity.ok(returnDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReturnDetail>> getAllReturnDetails() {
        List<ReturnDetail> returnDetails = returnDetailService.getAllReturnDetails();
        return ResponseEntity.ok(returnDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnDetail> updateReturnDetail(@PathVariable Long id, @RequestBody ReturnDetail returnDetail) {
        boolean updated = returnDetailService.updateReturnDetail(id, returnDetail);
        if (updated) {
            return ResponseEntity.ok(returnDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnDetail(@PathVariable Long id) {
        boolean deleted = returnDetailService.deleteReturnDetail(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/products/quantities/current-month")
    public List<ProductQuantity> getProductQuantitiesForCurrentMonth() {
        return returnDetailService.getProductQuantitiesForCurrentMonth();
    }

    @GetMapping("/products/quantities/by-month-year")
    public List<ProductQuantity> getProductQuantitiesForMonthYear(@RequestParam int month,
                                                                  @RequestParam int year) {
        return returnDetailService.getProductQuantitiesForMonthYear(month, year);
    }
    @GetMapping("/return-order/{invoiceId}")
    public List<InvoiceDetailResponse> getInvoiceDetailsByInvoiceId(@PathVariable Long invoiceId) {
        return returnDetailService.getReturnDetailsByInvoiceId(invoiceId);
    }
}
