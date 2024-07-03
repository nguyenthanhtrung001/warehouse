package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Location;

import java.util.List;

public interface ILocationService {
    Location createLocation(Location location);

    Location getLocationById(Long id);

    List<Location> getAllLocations();

    boolean updateLocation(Long id, Location location);

    boolean deleteLocation(Long id);
}
