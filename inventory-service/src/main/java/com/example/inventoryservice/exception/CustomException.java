package com.example.inventoryservice.exception;

public class CustomException extends RuntimeException {
    private final String message; // Thông điệp lỗi

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
