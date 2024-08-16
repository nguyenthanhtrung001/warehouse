package com.example.employeeservice.service;

import com.example.employeeservice.dto.request.WorkShiftRequest;
import com.example.employeeservice.entity.Attendance;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.entity.WorkShift;

import java.util.List;

public interface IAttendanceService {
    Attendance createAttendance(Attendance attendance);
    List<Attendance> createAttendance(WorkShiftRequest workShiftRequest);
    Attendance updateStatusAttendance(Long attendanceId, Integer status);

    Attendance getAttendanceById(Long id);
    Long countAttendanceByEmployee(Long employeeID);
    Double countHoursAttendanceByEmployee(Long employeeID);

    Long countHoursLateAttendanceByEmployee(Long employeeID);
    Long countHoursEarlyAttendanceByEmployee(Long employeeID);

    List<Attendance> getAllAttendances();

    boolean updateAttendance(Long id, Attendance attendance);
    boolean updateStatusAttendance(Long id);

    boolean deleteAttendance(Long id);
    public List<Attendance> getAttendancesByEmployeeId(Long employeeId);
    public List<Attendance> getAttendancesByEmployeeIdForCurrentMonth(Long employeeId);
    public List<Attendance> getAttendancesByEmployeeIdForCurrentWeek(Long employeeId);
    public List<Attendance> getAttendancesForCurrentMonth();
}
