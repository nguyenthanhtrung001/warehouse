package com.example.inventoryservice.exception;

public class LocationCapacityExceededException extends CustomException {
    public LocationCapacityExceededException(String message) {
        super(message);
    }
}
