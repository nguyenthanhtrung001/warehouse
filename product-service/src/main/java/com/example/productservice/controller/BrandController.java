package com.example.productservice.controller;

import com.example.productservice.entity.Brand;
import com.example.productservice.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final IBrandService brandService;

    public BrandController(IBrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity getAllBrands() {
         return new ResponseEntity<>(brandService.getAllBrands(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Brand getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id);
    }

    @PostMapping
    public ResponseEntity addBrand(@RequestBody Brand brand) {
         brandService.createBrand(brand);
         return new ResponseEntity<>("Brand created succesfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBrand(@PathVariable Long id,@RequestBody Brand brand) {
        brandService.updateBrand(brand,id);
        return new ResponseEntity<>("Brand update succesfully", HttpStatus.OK);

    }
}
