package com.example.productservice.repository;

import com.example.productservice.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  BrandRepository extends JpaRepository<Brand, Long> {
}
