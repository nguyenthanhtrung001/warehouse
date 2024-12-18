package com.example.goodsservice.service;

import com.example.goodsservice.dto.WarehouseBranch;
import com.example.goodsservice.entity.Warehouse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IWarehouseService {
    public List<Warehouse> getAllWarehousesExcludingId(Long id);
    public List<Warehouse> getAllWarehouses();
    public Warehouse addWarehouse(Warehouse warehouse);
    public void deleteWarehouse(Long id);
    public List<WarehouseBranch> getALLBranchRevenue();
    public String updateWarehouse(Long id, Warehouse updatedWarehouse);
    public Double getQuantityWarehouse(Long warehouseId);

    public  Warehouse getWarehouseId(Long warehouseId);

}
