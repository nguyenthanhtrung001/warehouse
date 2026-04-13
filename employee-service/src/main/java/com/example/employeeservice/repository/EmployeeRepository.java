package com.example.employeeservice.repository;

import com.example.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT e FROM Employee e WHERE e.position <> 'admin'  AND e.status <> 0")
    List<Employee> findAllNonAdminEmployees();
    @Query("SELECT e FROM Employee e WHERE e.warehouseId = :warehouseId AND e.status <> 0 AND e.id <> :employeeId AND (e.accountId IS NULL OR e.accountId <> 'admin' )")
    List<Employee> findAllNonAdminEmployeesByWarehouseIdAndNotEmployeeId(@Param("warehouseId") Long warehouseId, @Param("employeeId") Long employeeId);

    // Phương thức trả về danh sách các accountId không null theo warehouseId
    @Query("SELECT e.accountId FROM Employee e WHERE e.warehouseId = :warehouseId AND e.accountId IS NOT NULL")
    List<String> findAccountIdsByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Modifying
    @Query("UPDATE Employee e SET e.status = 0 WHERE e.id = :id")
    void deactivateEmployee(@Param("id") Long id);
    boolean existsByPhoneNumber(String phoneNumber);

    // Kiểm tra sự tồn tại của employee bằng email
    boolean existsByEmail(String email);
}