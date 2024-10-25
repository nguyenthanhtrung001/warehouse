package com.example.employeeservice.repository;

import com.example.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.accountId = :accountId")
    Employee findByAccountId(@Param("accountId") String accountId);

    @Query("SELECT e.employeeName FROM Employee e WHERE e.id = :id")
    String findEmployeeNameById(@Param("id") Long id);

    @Query("SELECT e FROM Employee e WHERE e.accountId <> 'admin'")
    List<Employee> findAllNonAdminEmployees();
    @Query("SELECT e FROM Employee e WHERE e.warehouseId = :warehouseId AND e.id <> :employeeId AND (e.accountId IS NULL OR e.accountId <> 'admin')")
    List<Employee> findAllNonAdminEmployeesByWarehouseIdAndNotEmployeeId(@Param("warehouseId") Long warehouseId, @Param("employeeId") Long employeeId);

}