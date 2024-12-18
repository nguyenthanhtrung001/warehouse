package com.example.orderservice.controller;

import com.example.orderservice.dto.InvoiceRequest;
import com.example.orderservice.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<?>> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        try {
            Invoice createdInvoice = invoiceService.createInvoice(invoiceRequest);
            // Trả về thông tin thành công cùng với đối tượng Invoice
            ApiResponse<Invoice> response = new ApiResponse<>(true, "Invoice created successfully", createdInvoice);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi khi có ngoại lệ IllegalArgumentException
            ApiResponse<String> errorResponse = new ApiResponse<>(false, "Invalid request", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Xử lý lỗi chung và ghi log chi tiết lỗi
            e.printStackTrace();
            ApiResponse<String> errorResponse = new ApiResponse<>(false, "Internal server error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
    public ResponseEntity<List<Invoice>> getAllInvoices(@RequestParam Long warehouseId) {
        List<Invoice> invoices = invoiceService.getAllInvoices(warehouseId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(
            @RequestParam Integer status,
            @RequestParam Long warehouseId) { // Thêm tham số warehouseId
        List<Invoice> invoices = invoiceService.getInvoicesByStatusAndWarehouseId(status, warehouseId);
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
    public ResponseEntity<String> updateInvoiceStatusPayment(@PathVariable("invoiceId") Long invoiceId){
        boolean isUpdated = invoiceService.updateInvoiceStatusPayment(invoiceId, 2);
        if (isUpdated) {
            return new ResponseEntity<>("Hóa đơn đã được cập nhật trạng thái thành công.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Hóa đơn không tồn tại.", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/product-summary")
    public Map<String, Object> getProductSummary( @RequestParam int year) {
        return invoiceService.getProductSalesSummary( year);
    }
    @GetMapping("/product-summary-warehouse")
    public Map<String, Object> getProductSummary(
            @RequestParam int year,
            @RequestParam Long wareHouseId) {  // Thêm tham số wareHouseId
        return invoiceService.getProductSalesSummary(year, wareHouseId); // Gọi phương thức với tham số mới
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Invoice>> getInvoicesByCustomerId(@PathVariable Long customerId) {
        List<Invoice> invoices = invoiceService.getInvoicesByCustomerId(customerId);
        if (invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }

}