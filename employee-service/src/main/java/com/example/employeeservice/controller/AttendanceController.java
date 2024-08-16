package com.example.employeeservice.controller;

import com.example.employeeservice.dto.request.WorkShiftRequest;
import com.example.employeeservice.entity.Attendance;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.entity.WorkShift;
import com.example.employeeservice.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    @Autowired
    private  IAttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<List<Attendance>> createAttendance(@RequestBody WorkShiftRequest workShiftRequest) {
        List<Attendance> attendance = attendanceService.createAttendance(workShiftRequest);
        return ResponseEntity.ok(attendance);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Attendance> updateStatusAttendance(
            @PathVariable("id") Long attendanceId,
            @RequestParam("status") Integer status) {
        Attendance updatedAttendance = attendanceService.updateStatusAttendance(attendanceId, status);
        if (updatedAttendance != null) {
            return new ResponseEntity<>(updatedAttendance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable("id") Long id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendances() {
        List<Attendance> attendances = attendanceService.getAllAttendances();
        return ResponseEntity.ok(attendances);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAttendance(@PathVariable("id") Long id, @RequestBody Attendance attendance) {
        boolean updated = attendanceService.updateAttendance(id, attendance);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Void> updateStatusAttendance(@PathVariable("id") Long id) {
        boolean updated = attendanceService.updateStatusAttendance(id);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable("id") Long id) {
        boolean deleted = attendanceService.deleteAttendance(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/countByEmployee/{employeeId}")
    public ResponseEntity<Long> countAttendanceByEmployee(@PathVariable Long employeeId) {
        Long count = attendanceService.countAttendanceByEmployee(employeeId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    @GetMapping("/count-hours/{employeeId}")
    public ResponseEntity<Double> countHoursAttendanceByEmployee(@PathVariable Long employeeId) {
        Double totalHours = attendanceService.countHoursAttendanceByEmployee(employeeId);
        return ResponseEntity.ok(totalHours);
    }

    @GetMapping("/lateHours/{employeeId}")
    public ResponseEntity<Long> countLateHoursByEmployee(@PathVariable Long employeeId) {
        Long lateHours = attendanceService.countHoursLateAttendanceByEmployee(employeeId);
        return ResponseEntity.ok(lateHours);
    }
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Attendance>> getAttendancesByEmployeeId(@PathVariable Long employeeId) {
        List<Attendance> attendances = attendanceService.getAttendancesByEmployeeId(employeeId);
        return ResponseEntity.ok(attendances);
    }
    @GetMapping("/employee/{employeeId}/current-month")
    public ResponseEntity<List<Attendance>> getAttendancesByEmployeeIdForCurrentMonth(@PathVariable Long employeeId) {
        List<Attendance> attendances = attendanceService.getAttendancesByEmployeeIdForCurrentMonth(employeeId);
        return ResponseEntity.ok(attendances);
    }
    @GetMapping("/employee/{employeeId}/current-week")
    public ResponseEntity<List<Attendance>> getAttendancesByEmployeeIdForCurrentWeek(@PathVariable Long employeeId) {
        List<Attendance> attendances = attendanceService.getAttendancesByEmployeeIdForCurrentWeek(employeeId);
        return ResponseEntity.ok(attendances);
    }
    @GetMapping("/current-month")
    public ResponseEntity<List<Attendance>> getAttendancesForCurrentMonth() {
        List<Attendance> attendances = attendanceService.getAttendancesForCurrentMonth();
        return ResponseEntity.ok(attendances);
    }
}
