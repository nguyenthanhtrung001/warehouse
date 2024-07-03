package com.example.goodsservice.service;

import com.example.goodsservice.entity.Supplier;

import java.util.List;

public interface ISupplierService {
    Supplier createSupplier(Supplier supplier);
    Supplier getSupplierById(Long id);
    List<Supplier> getAllSuppliers();
    boolean updateSupplier(Long id, Supplier supplierDetails);
    boolean deleteSupplier(Long id);
}
