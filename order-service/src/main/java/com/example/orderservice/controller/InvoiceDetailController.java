package com.example.orderservice.controller;

import com.example.orderservice.entity.InvoiceDetail;

import com.example.orderservice.service.IInvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-details")
public class InvoiceDetailController {
    @Autowired
    private IInvoiceDetailService invoiceDetailService;

    @PostMapping
    public ResponseEntity<InvoiceDetail> createInvoiceDetail(@RequestBody InvoiceDetail invoiceDetail) {
        InvoiceDetail createdInvoiceDetail = invoiceDetailService.createInvoiceDetail(invoiceDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoiceDetail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDetail> getInvoiceDetailById(@PathVariable Long id) {
        InvoiceDetail invoiceDetail = invoiceDetailService.getInvoiceDetailById(id);
        if (invoiceDetail != null) {
            return ResponseEntity.ok(invoiceDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDetail>> getAllInvoiceDetails() {
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.getAllInvoiceDetails();
        return ResponseEntity.ok(invoiceDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDetail> updateInvoiceDetail(@PathVariable Long id, @RequestBody InvoiceDetail invoiceDetail) {
        boolean updated = invoiceDetailService.updateInvoiceDetail(id, invoiceDetail);
        if (updated) {
            return ResponseEntity.ok(invoiceDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceDetail(@PathVariable Long id) {
        boolean deleted = invoiceDetailService.deleteInvoiceDetail(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
