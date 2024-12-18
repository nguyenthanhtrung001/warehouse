package com.example.goodsservice.validation;

import com.example.goodsservice.dto.Import_Export_DetailRequest;
import com.example.goodsservice.dto.Import_Export_Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImportExportRequestValidator {

    public static List<String> validateRequest(Import_Export_Request request) {
        List<String> errors = new ArrayList<>();

        // Kiểm tra Batch Name
        if (request.getBatchName() == null || request.getBatchName().trim().isEmpty()) {
            errors.add("Batch Name không được để trống.");
        }

        // Kiểm tra Expiry Date
        if (request.getExpiryDate() != null && request.getExpiryDate().before(new Date())) {
            errors.add("Hạn sử dụng phải lớn hơn ngày hiện tại.");
        }

        // Kiểm tra Warehouse ID
        if (request.getWarehouseId() == null || request.getWarehouseId() <= 0) {
            errors.add("Warehouse ID không được để trống và phải lớn hơn 0.");
        }

        // Kiểm tra Supplier
        if (request.getSupplier() == null || request.getSupplier() <= 0) {
            errors.add("Supplier không được để trống và phải lớn hơn 0.");
        }

        // Kiểm tra Employee ID
        if (request.getEmployeeId() == null || request.getEmployeeId() <= 0) {
            errors.add("Employee ID không được để trống và phải lớn hơn 0.");
        }

        // Kiểm tra danh sách Import_Export_Details
        if (request.getImport_Export_Details() == null || request.getImport_Export_Details().isEmpty()) {
            errors.add("Danh sách Import_Export_Details không được để trống.");
        } else {
            for (Import_Export_DetailRequest detail : request.getImport_Export_Details()) {
                if (detail.getProduct_Id() == null || detail.getProduct_Id() <= 0) {
                    errors.add("Product ID trong danh sách Import_Export_Details không được để trống và phải lớn hơn 0.");
                }
                if (detail.getQuantity() == null || detail.getQuantity() <= 0) {
                    errors.add("Quantity trong danh sách Import_Export_Details không được để trống và phải lớn hơn 0.");
                }
                if (detail.getPurchasePrice() == null || detail.getPurchasePrice() <= 0) {
                    errors.add("Purchase Price trong danh sách Import_Export_Details không được để trống và phải lớn hơn 0.");
                }
            }
        }

        return errors;
    }
}
