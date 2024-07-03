package com.example.productservice.service.impl;

import com.example.productservice.entity.Brand;
import com.example.productservice.entity.ProductGroup;
import com.example.productservice.repository.ProductGroupRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.IProductGroupService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implProductGroupService implements IProductGroupService {
     private ProductGroupRepository productGroupRepository;

    public implProductGroupService(ProductGroupRepository productGroupRepository) {
        this.productGroupRepository = productGroupRepository;
    }

    @Override
    public List<ProductGroup> getAllProductGroups() {
        return productGroupRepository.findAll();
    }

    @Override
    public ProductGroup getProductGroupById(Long id) {
        return productGroupRepository.findById(id).orElse(null);
    }

    @Override
    public ProductGroup createProductGroup(ProductGroup productGroup) {
        return productGroupRepository.save(productGroup);
    }

    @Override
    public boolean updateProductGroup(ProductGroup productGroup, Long id) {
        Optional<ProductGroup> productGroupOption= productGroupRepository.findById(id);
        if(productGroupOption.isPresent()){
            ProductGroup productGroupdUpdate= productGroupOption.get();
            productGroupdUpdate.setGroupName(productGroup.getGroupName());
            productGroupRepository.save(productGroupdUpdate);
            return true;
        }else return false;
    }

    @Override
    public boolean deleteProductGroup(Long id) {
        try {
            productGroupRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
