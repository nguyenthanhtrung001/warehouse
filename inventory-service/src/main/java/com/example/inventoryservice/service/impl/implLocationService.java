package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.repository.LocationRepository;
import com.example.inventoryservice.service.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class implLocationService implements ILocationService {
    @Autowired
    private  LocationRepository locationRepository;



    @Override
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location getLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        return location.orElse(null);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public boolean updateLocation(Long id, Location location) {
        if (locationRepository.existsById(id)) {
            location.setWarehouseLocation(location.getWarehouseLocation());
            locationRepository.save(location);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
