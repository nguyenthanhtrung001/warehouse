package com.example.productservice.service.impl;

import com.example.productservice.entity.Brand;
import com.example.productservice.repository.BrandRepository;
import com.example.productservice.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

@Service
public class implBrandService implements IBrandService {

    private BrandRepository  brandRepository;

    public implBrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Boolean updateBrand(Brand brand, Long id) {
        Optional<Brand> brandOption= brandRepository.findById(id);
        if(brandOption.isPresent()){
            Brand brandUpdate= brandOption.get();
            brandUpdate.setBrandName(brand.getBrandName());
            brandRepository.save(brandUpdate);
            return true;
        }else return false;

    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public boolean deleteBrand(Long id) {
        try {
            brandRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
