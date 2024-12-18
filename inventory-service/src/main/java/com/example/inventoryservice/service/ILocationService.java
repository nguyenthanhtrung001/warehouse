package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Location;

import java.util.List;

public interface ILocationService {
    Location createLocation(Location location);

    Location getLocationById(Long id);

    List<Location> getAllLocations();
    public List<Location> getAllLocationsForWarehouse(Long warehouseId);
    boolean updateLocation(Long id, Location location);

    boolean deleteLocation(Long id);
    public  Long getQuantityAllLocation(Long warehouseId);
}
