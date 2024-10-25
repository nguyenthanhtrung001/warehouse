package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryCheckSlipRequest;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.InventoryCheckDetail;
import com.example.inventoryservice.entity.InventoryCheckSlip;

import java.util.List;

public interface IInventoryCheckSlipService {

    InventoryCheckSlip createInventoryCheckSlip(InventoryCheckSlipRequest inventoryCheckSlipRequest);

    InventoryCheckSlip getInventoryCheckSlipById(Long id);
    List<InventoryCheckDetail> getDetailCheckSlipById( Long id);

    public List<InventoryCheckSlip> getAllInventoryCheckSlips(Long warehouseId);

    InventoryCheckSlip updateInventoryCheckSlip(Long id, InventoryCheckSlipRequest inventoryCheckSlipRequest);

    boolean deleteInventoryCheckSlip(Long id);
    public List<ProductQuantity> getProductDiscrepanciesForCurrentMonth();
    public List<ProductQuantity> getProductDiscrepanciesForMonthYear(int month, int year);
    public List<ProductQuantity> getProductDiscrepanciesForMonthYear(int month, int year, Long warehouseId);

    public List<ProductQuantity> getProductDiscrepanciesLessForMonthYear(int month, int year);
    public List<ProductQuantity> getProductDiscrepanciesLessForMonthYear(int month, int year, Long warehouseId);
    public List<ProductQuantity> getProductDiscrepanciesLessForCurrentMonth();




    }
