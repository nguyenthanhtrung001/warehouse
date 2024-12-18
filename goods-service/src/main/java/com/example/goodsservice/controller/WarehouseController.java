package com.example.goodsservice.controller;

import com.example.goodsservice.dto.WarehouseBranch;
import com.example.goodsservice.dto.response.ApiResponse;
import com.example.goodsservice.entity.Warehouse;
import com.example.goodsservice.service.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    private IWarehouseService warehouseService;

    @GetMapping("/exclude/{id}")
    public List<Warehouse> getAllWarehousesExcludingId(@PathVariable("id") Long id) {
        return warehouseService.getAllWarehousesExcludingId(id);
    }
    @GetMapping("")
    public List<Warehouse> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }
    @PostMapping
    public ResponseEntity<Warehouse> addWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse savedWarehouse = warehouseService.addWarehouse(warehouse);
        return ResponseEntity.ok(savedWarehouse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateWarehouse(@PathVariable Long id, @RequestBody Warehouse updatedWarehouse) {
        // Gọi service để thực hiện cập nhật kho
        String result = warehouseService.updateWarehouse(id, updatedWarehouse);

        // Kiểm tra nếu kết quả trả về là thành công
        if (result.equals("ok")) {
            // Trả về mã trạng thái 200 OK với thông báo thành công và dữ liệu trả về
            ApiResponse<String> response = new ApiResponse<>(true, "Cập nhật kho thành công", result);
            return ResponseEntity.ok(response);
        } else {
            // Trả về mã trạng thái 400 Bad Request với thông báo lỗi
            ApiResponse<String> response = new ApiResponse<>(false, result, result);
            return ResponseEntity.badRequest().body(response);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok("Warehouse with id " + id + " has been deleted.");
    }
    @GetMapping("/all-revenue")
    public List<WarehouseBranch> getAllBranchRevenue() {
        return warehouseService.getALLBranchRevenue();
    }

    @GetMapping("/capacity/{id}")
    public Double getQuantityWarehouse(@PathVariable Long id) {
        return warehouseService.getQuantityWarehouse(id);
    }


}
