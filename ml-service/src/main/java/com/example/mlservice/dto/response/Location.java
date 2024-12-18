package com.example.mlservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


public class Location {



    private Long id;

    private Long warehouseId;

    private String warehouseLocation;

    private LocationStatus status;

    private Long capacity;

    private Long currentLoad;


    // ===== Constructors =====
    public Location() {}

    public Location(Long id) {
        this.id = id;
    }

    public Location(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    // ===== Getters and Setters =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public LocationStatus getStatus() {
        return status;
    }

    public void setStatus(LocationStatus status) {
        this.status = status;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(Long currentLoad) {
        this.currentLoad = currentLoad;
    }
}
