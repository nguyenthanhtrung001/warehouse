package com.example.inventoryservice.exception;

public class InsufficientStockException extends RuntimeException {
    // Constructor mặc định
    public InsufficientStockException() {
        super("Không đủ hàng trong kho");
    }

    // Constructor với thông báo lỗi tùy chỉnh
    public InsufficientStockException(String message) {
        super(message);
    }

    // Constructor với thông báo lỗi và nguyên nhân
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor với nguyên nhân
    public InsufficientStockException(Throwable cause) {
        super(cause);
    }
}
