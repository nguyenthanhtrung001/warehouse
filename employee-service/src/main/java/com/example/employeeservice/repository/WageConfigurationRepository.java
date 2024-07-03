package com.example.employeeservice.repository;

import com.example.employeeservice.entity.WageConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WageConfigurationRepository extends JpaRepository<WageConfiguration, Long> {
}
