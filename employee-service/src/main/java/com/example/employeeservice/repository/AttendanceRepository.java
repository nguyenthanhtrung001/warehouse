package com.example.employeeservice.repository;

import com.example.employeeservice.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Long countByEmployeeIdAndWorkDateBetweenAndStatus(Long employeeId, LocalDate startOfMonth, LocalDate endOfMonth, int i);
    List<Attendance> findByEmployeeIdAndWorkDateBetweenAndStatus(Long employeeID, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth, int i);

    List<Attendance> findByEmployeeId(Long employeeId);

    List<Attendance> findByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate startOfMonth, LocalDate endOfMonth);

    List<Attendance> findAllByWorkDateBetween(LocalDate startOfMonth, LocalDate endOfMonth);
}
