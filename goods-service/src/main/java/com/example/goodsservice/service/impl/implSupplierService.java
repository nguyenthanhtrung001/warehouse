package com.example.goodsservice.service.impl;

import com.example.goodsservice.entity.Supplier;
import com.example.goodsservice.repository.SupplierRepository;
import com.example.goodsservice.service.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implSupplierService implements ISupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Supplier createSupplier(Supplier supplier) {
        // Kiểm tra nếu số điện thoại đã tồn tại
        if (supplierRepository.existsByPhoneNumber(supplier.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        // Kiểm tra nếu email đã tồn tại
        if (supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new DataIntegrityViolationException("Email đã tồn tại trong hệ thống.");
        }
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        return optionalSupplier.orElse(null);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public boolean updateSupplier(Long id, Supplier supplierDetails) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        if (optionalSupplier.isPresent()) {
            Supplier supplier = optionalSupplier.get();
            supplier.setSupplierName(supplierDetails.getSupplierName());
            supplier.setPhoneNumber(supplierDetails.getPhoneNumber());
            supplier.setAddress(supplierDetails.getAddress());
            supplier.setEmail(supplierDetails.getEmail());
            supplier.setNote(supplierDetails.getNote());
            supplierRepository.save(supplier);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteSupplier(Long id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
