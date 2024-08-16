package com.example.employeeservice.service.impl;

import com.example.employeeservice.entity.WageConfiguration;
import com.example.employeeservice.repository.WageConfigurationRepository;
import com.example.employeeservice.service.IWageConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implIWageConfigurationService implements IWageConfigurationService {
    @Autowired
    private WageConfigurationRepository wageConfigurationRepository;

    @Override
    public WageConfiguration createWageConfiguration(WageConfiguration wageConfiguration) {
        wageConfigurationRepository.deleteAll();
        return wageConfigurationRepository.save(wageConfiguration);
    }

    @Override
    public WageConfiguration getWageConfigurationById(Long id) {
        Optional<WageConfiguration> optionalWageConfiguration = wageConfigurationRepository.findById(id);
        return optionalWageConfiguration.orElse(null);
    }

    @Override
    public WageConfiguration getAll() {
        List<WageConfiguration> configuration = wageConfigurationRepository.findAll();
        WageConfiguration wageConfiguration = new WageConfiguration();
        if (configuration != null)
        {
            wageConfiguration = configuration.get(0);
        }
        return wageConfiguration;
    }

    @Override
    public WageConfiguration updateWageConfiguration(Long id, WageConfiguration wageConfiguration) {
        Optional<WageConfiguration> optionalWageConfiguration = wageConfigurationRepository.findById(id);
        if (optionalWageConfiguration.isPresent()) {
            WageConfiguration existingWageConfiguration = optionalWageConfiguration.get();
             existingWageConfiguration.setLatePenalty(wageConfiguration.getLatePenalty());
            existingWageConfiguration.setBonus(wageConfiguration.getBonus());
            existingWageConfiguration.setEarlyPenalty(wageConfiguration.getEarlyPenalty());
            return wageConfigurationRepository.save(existingWageConfiguration);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteWageConfiguration(Long id) {
        Optional<WageConfiguration> optionalWageConfiguration = wageConfigurationRepository.findById(id);
        if (optionalWageConfiguration.isPresent()) {
            wageConfigurationRepository.delete(optionalWageConfiguration.get());
            return true;
        } else {
            return false;
        }
    }
}