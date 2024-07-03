package com.example.productservice.service;

import com.example.productservice.entity.Brand;

import java.util.List;

public interface IBrandService {

    public List<Brand> getAllBrands();
    Boolean updateBrand(Brand brand, Long id);
    public Brand getBrandById(Long id);
    public Brand createBrand(Brand brand);
    public boolean deleteBrand(Long id);
}
