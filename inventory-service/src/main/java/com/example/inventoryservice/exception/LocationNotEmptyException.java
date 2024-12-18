package com.example.inventoryservice.exception;
public class LocationNotEmptyException extends RuntimeException {
    public LocationNotEmptyException(String message) {
        super(message);
    }
}

