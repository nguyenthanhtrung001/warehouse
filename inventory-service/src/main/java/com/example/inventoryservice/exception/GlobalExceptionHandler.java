package com.example.inventoryservice.exception;


import com.example.inventoryservice.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý ngoại lệ LocationCapacityExceededException
    @ExceptionHandler(LocationCapacityExceededException.class)
    public ResponseEntity<ApiResponse<?>> handleLocationCapacityExceeded(LocationCapacityExceededException ex) {
        ApiResponse<?> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý ngoại lệ chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        ApiResponse<?> response = new ApiResponse<>(false, "Lỗi hệ thống: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
