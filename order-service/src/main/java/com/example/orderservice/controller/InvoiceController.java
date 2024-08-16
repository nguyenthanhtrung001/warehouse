package com.example.orderservice.controller;

import com.example.orderservice.dto.InvoiceRequest;
import com.example.orderservice.entity.Invoice;
import com.example.orderservice.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private  IInvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        try {
            Invoice createdInvoice = invoiceService.createInvoice(invoiceRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }
    @GetMapping("/status")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(@RequestParam Integer status) {
        List<Invoice> invoices = invoiceService.getAllInvoicesWithStatus(status);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        boolean updated = invoiceService.updateInvoice(id, invoice);
        if (updated) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        boolean deleted = invoiceService.deleteInvoiceWithStatus_1(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{invoiceId}/status")
    public ResponseEntity<String> updateInvoiceStatus(@PathVariable("invoiceId") Long invoiceId){
        boolean isUpdated = invoiceService.updateInvoiceStatus(invoiceId, 2);
        if (isUpdated) {
            return new ResponseEntity<>("Hóa đơn đã được cập nhật trạng thái thành công.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Hóa đơn không tồn tại.", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/api/product-summary")
    public Map<String, Object> getProductSummary( @RequestParam int year) {
        return invoiceService.getProductSalesSummary( year);
    }

}