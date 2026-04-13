package com.example.employeeservice.repository;

import com.example.employeeservice.entity.QCKPIConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface QCKPIRepository extends JpaRepository<QCKPIConfig, Long> {

    Optional<QCKPIConfig> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}