package com.example.productservice.service;


import com.example.productservice.entity.ProductGroup;

import java.util.List;

public interface  IProductGroupService {
    List<ProductGroup> getAllProductGroups();
    ProductGroup getProductGroupById(Long id);
    ProductGroup createProductGroup(ProductGroup productGroup);
    boolean updateProductGroup(ProductGroup brand, Long id);
    boolean deleteProductGroup(Long id);
}
