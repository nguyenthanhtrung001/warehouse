package com.example.productservice.repository;

import com.example.productservice.entity.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ProductGroupRepository extends JpaRepository<ProductGroup, Long> {

}
