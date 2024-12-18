package com.example.goodsservice.controller;

import com.example.goodsservice.dto.response.ApiResponse;
import com.example.goodsservice.entity.Supplier;
import com.example.goodsservice.service.ISupplierService;
import com.example.goodsservice.validation.SupplierValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@Validated
public class SupplierController {

    @Autowired
    private ISupplierService supplierService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(@RequestBody Supplier supplier) {
        // Validate the supplier using the SupplierValidator
        List<String> validationErrors = SupplierValidator.validate(supplier);

        if (!validationErrors.isEmpty()) {
            // Nếu có lỗi validation, trả về lỗi
            String errorMessage = String.join(", ", validationErrors);
            ApiResponse<Supplier> apiResponse = new ApiResponse<>(false, "Validation failed: " + errorMessage, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        try {
            // Nếu dữ liệu hợp lệ, tạo nhà cung cấp
            Supplier createdSupplier = supplierService.createSupplier(supplier);

            // Trả về thông báo thành công
            ApiResponse<Supplier> apiResponse = new ApiResponse<>(true, "Nhà cung cấp đã được tạo thành công", createdSupplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            // Handle errors such as duplicate phone or email
            ApiResponse<Supplier> apiResponse = new ApiResponse<>(false, "Lỗi: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    // Exception handler for DataIntegrityViolationException (duplicate phone or email)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ApiResponse<String> apiResponse = new ApiResponse<>(false, "Lỗi: " + e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier != null) {
            return ResponseEntity.ok(supplier);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplierDetails) {
        boolean updated = supplierService.updateSupplier(id, supplierDetails);
        if (updated) {
            return ResponseEntity.ok(supplierDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        boolean deleted = supplierService.deleteSupplier(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}