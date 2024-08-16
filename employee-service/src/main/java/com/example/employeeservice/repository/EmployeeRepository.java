package com.example.employeeservice.repository;

import com.example.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.account_id = :accountId")
    Employee findByAccountId(@Param("accountId") String accountId);

    @Query("SELECT e.employeeName FROM Employee e WHERE e.id = :id")
    String findEmployeeNameById(@Param("id") Long id);

    @Query("SELECT e FROM Employee e WHERE e.account_id <> 'admin'")
    List<Employee> findAllNonAdminEmployees();
}