package com.example.employeeservice.controller;

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



 /*   public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        Attendance createdAttendance = attendanceService.createAttendance(attendance);
        return new ResponseEntity<>(createdAttendance, HttpStatus.CREATED);
    }*/

    @PostMapping("/create")
    public ResponseEntity<Attendance> createAttendanceByEmployeeAndWorkShift(
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("workShiftId") Long workShiftId) {
        // Tạo đối tượng Employee và WorkShift
        Employee employee = new Employee();
        employee.setId(employeeId);

        WorkShift workShift = new WorkShift();
        workShift.setId(workShiftId);

        // Tạo Attendance bằng cách sử dụng phương thức của service
        Attendance createdAttendance = attendanceService.createAttendance(employee, workShift);

        // Trả về ResponseEntity chứa Attendance vừa tạo và HTTP status CREATED
        return new ResponseEntity<>(createdAttendance, HttpStatus.CREATED);
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
}
