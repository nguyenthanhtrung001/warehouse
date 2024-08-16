package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.request.WorkShiftRequest;
import com.example.employeeservice.entity.Attendance;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.entity.WorkShift;
import com.example.employeeservice.repository.AttendanceRepository;
import com.example.employeeservice.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implAttendanceService implements IAttendanceService {

    @Autowired
    private  AttendanceRepository attendanceRepository;

    @Override
    public Attendance createAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public Long countAttendanceByEmployee(Long employeeId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        return attendanceRepository.countByEmployeeIdAndWorkDateBetweenAndStatus(
                employeeId, startOfMonth, endOfMonth, 1);
    }

    @Override
    public Double countHoursAttendanceByEmployee(Long employeeID) {
        // Lấy ngày đầu và ngày cuối của tháng hiện tại
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        // Lấy danh sách các Attendance của Employee có workDate trong tháng hiện tại và status=1
        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndWorkDateBetweenAndStatus(employeeID, firstDayOfMonth, lastDayOfMonth, 2);

        // Tính tổng số giờ làm việc của Employee trong tháng
        System.out.println("so ca:"+attendances.size());
        double totalHours = 0;

        for (Attendance attendance : attendances) {
            LocalTime startTime = attendance.getStartTime();
            LocalTime endTime = attendance.getEndTime();
            System.out.println("Id: " + attendance.getId());
            System.out.println("Start Time: " + startTime);
            System.out.println("End Time: " + endTime);
            double hours = (Duration.between(startTime, endTime).toMinutes())/60.0;
            double hours_Shift = (Duration.between(attendance.getWorkShift().getStartTime(), attendance.getWorkShift().getEndTime()).toMinutes())/60.0;
            System.out.println("hoaushift: " + hours_Shift);
            System.out.println("hose " + hours);
            if(hours_Shift<hours)
            {
                hours=hours_Shift;
            }
            totalHours += hours;
        }


        return totalHours;
    }

    @Override
    public Long countHoursLateAttendanceByEmployee(Long employeeID) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        // Truy vấn tất cả các Attendance của Employee có workDate trong tháng hiện tại với status=1
        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndWorkDateBetweenAndStatus(employeeID, startOfMonth, endOfMonth, 2);

        for ( Attendance attendance : attendances){
            System.out.println("tre tre:: "+ attendance.getLateTime());
        }

        // Tính tổng thời gian trễ
        long totalLateHours = attendances.stream()
                .filter(attendance -> attendance.getLateTime() != null && attendance.getLateTime() < 0)
                .mapToLong(Attendance::getLateTime)
                .sum();
        System.out.println("So tre la:"+totalLateHours);
        return totalLateHours;
    }

    @Override
    public Long countHoursEarlyAttendanceByEmployee(Long employeeID) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();


        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndWorkDateBetweenAndStatus(employeeID, startOfMonth, endOfMonth, 2);


        // Tính tổng thời gian trễ
        long totalEarlyHours = attendances.stream()
                .filter(attendance -> attendance.getEarlyTime() != null && attendance.getEarlyTime() < 0)
                .mapToLong(Attendance::getEarlyTime)
                .sum();
        System.out.println("Về som la:"+totalEarlyHours);
        return totalEarlyHours;
    }


    @Override
    public List<Attendance> createAttendance(WorkShiftRequest workShiftRequest) {

        List< Attendance> attendances = new ArrayList<>();
        for ( Long id : workShiftRequest.getWorkShiftId())
        {
            Attendance attendance = new Attendance();
            Employee employee = new Employee();
            employee.setId(workShiftRequest.getEmployeeId());
            WorkShift workShift = new WorkShift();
            workShift.setId(id);
            attendance.setEmployee(employee);
            attendance.setWorkShift(workShift);
            attendance.setWorkDate(workShiftRequest.getWorkDate());
            attendance.setStatus(1);
            attendances.add(attendance);
        }



        return attendanceRepository.saveAll(attendances);
    }

    @Override
    public Attendance updateStatusAttendance(Long attendanceId, Integer status) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceId);
        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();
            attendance.setStatus(status);
            return attendanceRepository.save(attendance);
        }
           return null;
    }


    @Override
    public Attendance getAttendanceById(Long id) {
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        return attendance.orElse(null);
    }

    @Override
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    @Override
    public boolean updateAttendance(Long id, Attendance attendanceDetails) {
        Optional<Attendance> existingAttendanceOpt = attendanceRepository.findById(id);
        if (existingAttendanceOpt.isPresent()) {
            Attendance existingAttendance = existingAttendanceOpt.get();

//            existingAttendance.setWorkShift(attendanceDetails.getWorkShift());
//            existingAttendance.setWorkDate(attendanceDetails.getWorkDate());
//
            existingAttendance.setStatus(2);
            existingAttendance.setNote(attendanceDetails.getNote());
            existingAttendance.setStartTime(attendanceDetails.getStartTime());
            existingAttendance.setEndTime(attendanceDetails.getEndTime());

            // Tính toán lastTimeInSeconds chỉ khi cả startTime và workShift.startTime đều không null
            if (existingAttendance.getStartTime() != null && existingAttendance.getWorkShift() != null && existingAttendance.getWorkShift().getStartTime() != null) {
                Duration duration = Duration.between(existingAttendance.getStartTime(), existingAttendance.getWorkShift().getStartTime());
                existingAttendance.setLateTime(duration.toMinutes());
                System.out.println("starts:"+existingAttendance.getStartTime());
                System.out.println("starts:"+existingAttendance.getWorkShift().getStartTime());

                System.out.println("Cos r nha:"+duration.getSeconds());
            } else {
                existingAttendance.setLateTime(0L);  // sử dụng chữ "L" để biểu thị giá trị long
            }

            if (existingAttendance.getEndTime() != null && existingAttendance.getWorkShift() != null && existingAttendance.getWorkShift().getEndTime() != null) {
                Duration duration = Duration.between(existingAttendance.getWorkShift().getEndTime(),existingAttendance.getEndTime());
                existingAttendance.setEarlyTime(duration.toMinutes());

            } else {
                existingAttendance.setEarlyTime(0L);  // sử dụng chữ "L" để biểu thị giá trị long
            }

            attendanceRepository.save(existingAttendance);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStatusAttendance(Long id) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(id);
        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();
            attendance.setStatus(1);
            attendanceRepository.save(attendance);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAttendance(Long id) {
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Attendance> getAttendancesByEmployeeId(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Attendance> getAttendancesByEmployeeIdForCurrentMonth(Long employeeId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        return attendanceRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startOfMonth, endOfMonth);

    }

    @Override
    public List<Attendance> getAttendancesByEmployeeIdForCurrentWeek(Long employeeId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        return attendanceRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startOfWeek, endOfWeek);

    }

    @Override
    public List<Attendance> getAttendancesForCurrentMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        return attendanceRepository.findAllByWorkDateBetween(startOfMonth, endOfMonth);

    }
}
