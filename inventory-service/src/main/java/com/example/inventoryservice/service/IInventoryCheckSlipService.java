package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryCheckSlipRequest;
import com.example.inventoryservice.entity.InventoryCheckSlip;

import java.util.List;

public interface IInventoryCheckSlipService {

    InventoryCheckSlip createInventoryCheckSlip(InventoryCheckSlipRequest inventoryCheckSlipRequest);

    InventoryCheckSlip getInventoryCheckSlipById(Long id);

    List<InventoryCheckSlip> getAllInventoryCheckSlips();

    InventoryCheckSlip updateInventoryCheckSlip(Long id, InventoryCheckSlipRequest inventoryCheckSlipRequest);

    boolean deleteInventoryCheckSlip(Long id);
}
