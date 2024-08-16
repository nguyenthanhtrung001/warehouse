package com.example.employeeservice.service;

import com.example.employeeservice.entity.WageConfiguration;

public interface IWageConfigurationService {

    WageConfiguration createWageConfiguration(WageConfiguration wageConfiguration);

    WageConfiguration getWageConfigurationById(Long id);
    WageConfiguration getAll();

    WageConfiguration updateWageConfiguration(Long id, WageConfiguration wageConfiguration);

    boolean deleteWageConfiguration(Long id);
}
