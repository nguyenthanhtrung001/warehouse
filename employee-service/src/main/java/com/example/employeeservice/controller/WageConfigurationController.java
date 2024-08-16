package com.example.employeeservice.controller;

import com.example.employeeservice.entity.WageConfiguration;
import com.example.employeeservice.service.IWageConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wage-configurations")
public class WageConfigurationController {

    @Autowired
    private  IWageConfigurationService wageConfigurationService;




    @PostMapping("/create")
    public ResponseEntity<WageConfiguration> createWageConfiguration(@RequestBody WageConfiguration wageConfiguration) {
        WageConfiguration createdWageConfiguration = wageConfigurationService.createWageConfiguration(wageConfiguration);
        return new ResponseEntity<>(createdWageConfiguration, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WageConfiguration> getWageConfigurationById(@PathVariable Long id) {
        WageConfiguration wageConfiguration = wageConfigurationService.getWageConfigurationById(id);
        if (wageConfiguration != null) {
            return new ResponseEntity<>(wageConfiguration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping()
    public ResponseEntity<WageConfiguration> getWageConfiguration() {
        WageConfiguration wageConfiguration = wageConfigurationService.getAll();
        if (wageConfiguration != null) {
            return new ResponseEntity<>(wageConfiguration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<WageConfiguration> updateWageConfiguration(@PathVariable Long id, @RequestBody WageConfiguration wageConfiguration) {
        WageConfiguration updatedWageConfiguration = wageConfigurationService.updateWageConfiguration(id, wageConfiguration);
        if (updatedWageConfiguration != null) {
            return new ResponseEntity<>(updatedWageConfiguration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWageConfiguration(@PathVariable Long id) {
        boolean deleted = wageConfigurationService.deleteWageConfiguration(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
