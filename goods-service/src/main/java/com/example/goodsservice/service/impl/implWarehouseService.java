package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.client.OrderClient;
import com.example.goodsservice.dto.WarehouseBranch;
import com.example.goodsservice.entity.Warehouse;
import com.example.goodsservice.repository.WarehouseRepository;
import com.example.goodsservice.service.IWarehouseService;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implWarehouseService  implements IWarehouseService {

    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    OrderClient orderClient;
    @Autowired
    InventoryClient inventoryClient;

    @Override
    public List<Warehouse> getAllWarehousesExcludingId(Long id) {
        return warehouseRepository.findAllExcludingId(id);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse addWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public void deleteWarehouse(Long id) {
        if (warehouseRepository.existsById(id)) {
            warehouseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Warehouse with id " + id + " not found.");
        }
    }

    @Override
    public List<WarehouseBranch> getALLBranchRevenue() {
       List <Warehouse> warehouses = getAllWarehouses();
       List <WarehouseBranch> warehouseBranches = new ArrayList<>();

        for (Warehouse warehouse : warehouses){
            WarehouseBranch branch = new WarehouseBranch();
            branch.setId(warehouse.getId());
            branch.setWarehouseName(warehouse.getWarehouseName());
            try{
                Long revenue = orderClient.getRevenueByWarehouseId(warehouse.getId());
                branch.setRevenue(revenue);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            warehouseBranches.add(branch);
        }
        return warehouseBranches;
    }

    @Override
    public String updateWarehouse(Long id, Warehouse updatedWarehouse) {
        // Tìm kho theo ID
        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(id);

        // Kiểm tra nếu kho tồn tại
        if (warehouseOpt.isPresent()) {
            Warehouse warehouse = warehouseOpt.get();

            // Cập nhật thông tin kho
            warehouse.setWarehouseName(updatedWarehouse.getWarehouseName());
            warehouse.setLocation(updatedWarehouse.getLocation());

            // Lấy tổng số lượng sản phẩm trong kho
            Long sumQuantityLocation = inventoryClient.getQuantityAllLocation(id);
            System.out.println("sl000:"+sumQuantityLocation);
            // Kiểm tra nếu sức chứa kho mới nhỏ hơn tổng số lượng sản phẩm trong kho
            if (updatedWarehouse.getCapacity() < sumQuantityLocation) {
                return "Kho đang chứa " + sumQuantityLocation + " sản phẩm, vui lòng cập nhật sức chứa >= " + sumQuantityLocation;
            }else {
                warehouse.setCapacity(updatedWarehouse.getCapacity());
            }
            // Kiểm tra và cập nhật số điện thoại nếu có
            if (updatedWarehouse.getPhoneNumber() != null) {

                // Cập nhật số điện thoại
                warehouse.setPhoneNumber(updatedWarehouse.getPhoneNumber());
            }

            // Cập nhật email nếu có
            if (updatedWarehouse.getEmail() != null) {
                warehouse.setEmail(updatedWarehouse.getEmail());
            }

            // Cập nhật ghi chú nếu có
            if (updatedWarehouse.getNote() != null) {
                warehouse.setNote(updatedWarehouse.getNote());
            }

            // Lưu lại thông tin kho sau khi cập nhật
            warehouseRepository.save(warehouse);

            return "ok";
        } else {
            // Nếu không tìm thấy kho theo ID
            return "Kho với ID " + id + " không tồn tại.";
        }
    }


    @Override
    public Double getQuantityWarehouse(Long warehouseId) {
       Warehouse warehouse = getWarehouseId(warehouseId);
       if (warehouse!= null){
           return warehouse.getCapacity();
       } return 0.0;
    }

    @Override
    public Warehouse getWarehouseId(Long warehouseId) {
       return warehouseRepository.findById(warehouseId).orElse(null);
    }

}
