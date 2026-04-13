package com.example.employeeservice.repository;

import com.example.employeeservice.dto.response.QCLeaderboardDTO;
import com.example.employeeservice.entity.QC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QCRepository extends JpaRepository<QC, Long> {

    boolean existsByQcCode(String qcCode);

    @Query("""
        SELECT q FROM QC q
        WHERE q.employeeId = :employeeId
        AND q.deleted = false
        AND q.scanTime >= :start
        AND q.scanTime < :end
        ORDER BY q.scanTime DESC
    """)
    List<QC> findByEmployeeAndTimeRange(
            Long employeeId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<QC> findByEmployeeIdAndDeletedFalseAndQcCodeContainingIgnoreCase(
            Long employeeId,
            String keyword
    );

    Optional<QC> findByIdAndDeletedFalse(Long id);

    @Query("""
    SELECT new com.example.employeeservice.dto.response.QCLeaderboardDTO(
        q.employeeId,
        e.employeeName,
        COUNT(q)
    )
    FROM QC q
    JOIN Employee e ON q.employeeId = e.id
    WHERE q.deleted = false
    AND q.scanTime >= :start
    AND q.scanTime < :end
    GROUP BY q.employeeId, e.employeeName
    ORDER BY COUNT(q) DESC
""")
    List<QCLeaderboardDTO> getLeaderboard(
            LocalDateTime start,
            LocalDateTime end
    );
}