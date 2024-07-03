package com.example.productservice.controller;

import com.example.productservice.entity.ProductGroup;
import com.example.productservice.service.IProductGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-groups")
public class ProductGroupController {


    private IProductGroupService productGroupService;

    public ProductGroupController(IProductGroupService productGroupService) {
        this.productGroupService = productGroupService;
    }

    // Lấy danh sách tất cả các ProductGroup
    @GetMapping
    public List<ProductGroup> getAllProductGroups() {
        return productGroupService.getAllProductGroups();
    }

    // Lấy ProductGroup theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductGroup> getProductGroupById(@PathVariable Long id) {
        ProductGroup productGroup = productGroupService.getProductGroupById(id);
        if (productGroup != null) {
            return ResponseEntity.ok(productGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo ProductGroup mới
    @PostMapping
    public ProductGroup createProductGroup(@RequestBody ProductGroup productGroup) {
        return productGroupService.createProductGroup(productGroup);
    }

    // Cập nhật ProductGroup
    @PutMapping("/{id}")
    public ResponseEntity<ProductGroup> updateProductGroup(@PathVariable Long id, @RequestBody ProductGroup productGroupDetails) {
        ProductGroup productGroup = productGroupService.getProductGroupById(id);
        if (productGroup != null) {
            productGroup.setGroupName(productGroupDetails.getGroupName());
            // Cập nhật các thuộc tính khác nếu có
            ProductGroup updatedProductGroup = productGroupService.createProductGroup(productGroup);
            return ResponseEntity.ok(updatedProductGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa ProductGroup
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductGroup(@PathVariable Long id) {
        ProductGroup productGroup = productGroupService.getProductGroupById(id);
        if (productGroup != null) {
            productGroupService.deleteProductGroup(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}