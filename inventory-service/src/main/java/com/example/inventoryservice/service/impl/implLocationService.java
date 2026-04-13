package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.client.GoodClient;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.exception.LocationCapacityExceededException;
import com.example.inventoryservice.exception.LocationNotEmptyException;
import com.example.inventoryservice.repository.LocationRepository;
import com.example.inventoryservice.service.IBatchDetailService;
import com.example.inventoryservice.service.ILocationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class implLocationService implements ILocationService {
    @Autowired
    private  LocationRepository locationRepository;
    @Autowired
    private IBatchDetailService batchDetailService;

    @Autowired
    private GoodClient goodClient;



    @Override
    public Location createLocation(Location location) {
        // Lấy sức chứa kho từ goodClient
        Double capacity = goodClient.getQuantityWarehouse(location.getWarehouseId());

        // Lấy danh sách các vị trí trong kho
        List<Location> locations = getAllLocationsForWarehouse(location.getWarehouseId());

        // Tính tổng tải trọng hiện tại của tất cả các vị trí
        Long totalLoad = getQuantityAllLocation(location.getWarehouseId());

        totalLoad += location.getCapacity();
        // Kiểm tra nếu tổng tải trọng đã vượt sức chứa của kho
        if (totalLoad >= capacity) {
            throw new LocationCapacityExceededException(
                    String.format("Tổng tải trọng hiện tại (%d) đã vượt sức chứa của kho (%f).", totalLoad, capacity)
            );
        }

        // Lưu vị trí mới nếu còn chỗ
        return locationRepository.save(location);
    }
    @Override
    public  Long getQuantityAllLocation(Long warehouseId){
        List<Location> locations = getAllLocationsForWarehouse(warehouseId);

        // Tính tổng tải trọng hiện tại của tất cả các vị trí
        Long totalLoad = locations.stream()
                .mapToLong(loc -> batchDetailService.getTotalQuantityByWarehouseAndLocation(loc.getId(), warehouseId))
                .sum();
        return  totalLoad;
    }

    @Override
    public Location getLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        Long currentLoad = batchDetailService.getTotalQuantityByWarehouseAndLocation(location.get().getId(), location.get().getWarehouseId());
        location.get().setCurrentLoad(currentLoad);
        return location.orElse(null);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
//    public List<Location> getAllLocationsForWarehouse(Long warehouseId) {
//        return locationRepository.findLocationByWarehouseId(warehouseId);
//    }
    public List<Location> getAllLocationsForWarehouse(Long warehouseId) {
        // Lấy danh sách tất cả các Location theo warehouseId
        List<Location> locations = locationRepository.findLocationByWarehouseId(warehouseId);

        // Cập nhật currentLoad cho từng Location bằng cách gọi batchDetailService
        locations.forEach(location -> {
            Long currentLoad = batchDetailService.getTotalQuantityByWarehouseAndLocation(location.getId(), warehouseId);

            System.out.println(location.getWarehouseLocation()+" sl: "+currentLoad+" ----- id: "+location.getId()+"----wh: "+warehouseId);
            location.setCurrentLoad(currentLoad != null ? currentLoad : 0);
        });

        return locations;
    }
    @Override
    public Long getCurrentLoadForLocationId(Long locationId, Long warehouseId){
        Location location = getLocationById(locationId);
        Long currentLoad = batchDetailService.getTotalQuantityByWarehouseAndLocation(locationId, warehouseId);
        Long inventory = location.getCapacity() - currentLoad;
        return currentLoad != null ? inventory : 0;

    }



    @Override
    public boolean updateLocation(Long id, Location location) {
        // Tìm vị trí hiện tại theo ID
        Location loca = locationRepository.findById(id).orElse(null);
        if (loca == null) {
            return false;
        }

        // Lấy sức chứa kho từ goodClient
        Double capacity = goodClient.getQuantityWarehouse(location.getWarehouseId());

        // Lấy danh sách các vị trí trong kho
        List<Location> locations = getAllLocationsForWarehouse(location.getWarehouseId());

        // Tính tổng tải trọng hiện tại (trừ vị trí đang cập nhật)
        Long totalLoad = locations.stream()
                .mapToLong(loc -> batchDetailService.getTotalQuantityByWarehouseAndLocation(loc.getId(), location.getWarehouseId()))
                .sum();

        // Lấy tải trọng hiện tại của vị trí cũ
        Long quantityOld = batchDetailService.getTotalQuantityByWarehouseAndLocation(loca.getId(), location.getWarehouseId());

        // Kiểm tra điều kiện: Sức chứa mới phải >= tải trọng hiện tại
        checkNewCapacity(location.getCapacity(), quantityOld);

        // Tính tổng tải trọng mới (bao gồm vị trí đang cập nhật)
        totalLoad += location.getCapacity();

        // Kiểm tra nếu tổng tải trọng vượt sức chứa kho
        checkWarehouseCapacity(totalLoad, capacity);

        // Cập nhật thông tin vị trí
        loca.setWarehouseLocation(location.getWarehouseLocation());
        loca.setCapacity(location.getCapacity());
        loca.setStatus(location.getStatus());
        locationRepository.save(loca);

        return true;
    }

    private void checkNewCapacity(Long newCapacity, Long currentLoad) {
        if (newCapacity < currentLoad) {
            throw new LocationCapacityExceededException(
                    String.format("Sức chứa mới (%d) phải lớn hơn hoặc bằng tải trọng hiện tại trong kho (%d).", newCapacity, currentLoad)
            );
        }
    }
    private void checkWarehouseCapacity(Long totalLoad, Double capacity) {
        if (totalLoad >= capacity) {
            throw new LocationCapacityExceededException(
                    String.format("Tổng tải trọng hiện tại (%d) đã vượt sức chứa của kho (%f).", totalLoad, capacity)
            );
        }
    }

    @Override
    public boolean deleteLocation(Long id) {
        try {
            // Kiểm tra xem vị trí có tồn tại
            if (!locationRepository.existsById(id)) {
                throw new EntityNotFoundException("Vị trí không tồn tại với ID: " + id);
            }
            // Xóa vị trí
            locationRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException ex) {
            // Bắt ngoại lệ nếu vi phạm ràng buộc khóa ngoại
            throw new LocationNotEmptyException("Không thể xóa vị trí vì nó đang được liên kết với các sản phẩm.");
        }
    }

}
