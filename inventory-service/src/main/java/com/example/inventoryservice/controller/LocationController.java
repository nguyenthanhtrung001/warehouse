package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.response.ApiResponse;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.exception.LocationCapacityExceededException;
import com.example.inventoryservice.exception.LocationNotEmptyException;
import com.example.inventoryservice.service.ILocationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private  ILocationService locationService;

    @PostMapping
    public ResponseEntity<?> createLocation(@RequestBody Location location) {
        try {
            // Gọi service để tạo vị trí
            Location createdLocation = locationService.createLocation(location);
            // Trả về phản hồi với trạng thái HTTP 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(true, "Vị trí đã được tạo thành công.", createdLocation)
            );
        } catch (LocationCapacityExceededException ex) {
            // Xử lý ngoại lệ vượt sức chứa kho
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, ex.getMessage(), null)
            );
        } catch (Exception ex) {
            // Xử lý lỗi chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Đã xảy ra lỗi không xác định.", null)
            );
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Location>> getAllLocationsInWarehouse(@PathVariable Long warehouseId) {
        List<Location> locations = locationService.getAllLocationsForWarehouse(warehouseId);
        return ResponseEntity.ok(locations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        try {
            boolean updated = locationService.updateLocation(id, location);

            if (updated) {
                return ResponseEntity.ok(
                        new ApiResponse<>(true, "Vị trí đã được cập nhật thành công.", location)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Không tìm thấy vị trí với ID: " + id, null));
            }
        } catch (LocationCapacityExceededException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Đã xảy ra lỗi trong quá trình cập nhật.", null));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (LocationNotEmptyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Đã xảy ra lỗi không xác định.", null));
        }
    }
    @GetMapping("/{warehouseId}/total-load")
    public Long getQuantityAllLocation(@PathVariable Long warehouseId) {
        return locationService.getQuantityAllLocation(warehouseId);
    }

}