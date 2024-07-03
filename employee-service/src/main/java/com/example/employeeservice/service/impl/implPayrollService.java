package com.example.employeeservice.service.impl;

import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.entity.Payroll;
import com.example.employeeservice.entity.WageConfiguration;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.repository.PayrollRepository;
import com.example.employeeservice.repository.WageConfigurationRepository;
import com.example.employeeservice.service.IAttendanceService;
import com.example.employeeservice.service.IPayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

@Service
public class implPayrollService implements IPayrollService {

    @Autowired
    private  PayrollRepository payrollRepository;
    @Autowired
    private WageConfigurationRepository wageConfigurationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private IAttendanceService attendanceService;


    @Override
    public Payroll createPayroll(Payroll payroll) {
        return payrollRepository.save(payroll);
    }

    @Override
    public Payroll createPayrollByEmployee(Long employeeId) {
        // Lấy thông tin nhân viên
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            // Xử lý khi không tìm thấy Employee với employeeId
            throw new RuntimeException("Employee not found");
        }

        Employee employee = optionalEmployee.get();

        // Lấy tháng và năm hiện tại
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // Tạo tên bảng lương
        String namePayroll = "Bảng lương tháng " + currentMonth + "/" + currentYear;

        // Tạo khoảng thời gian làm việc
        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        String workingPeriod = firstDayOfMonth.format(formatter) + " - " + lastDayOfMonth.format(formatter);

        // Tạo đối tượng Payroll
        Payroll payroll = new Payroll();
        payroll.setNamePayroll(namePayroll);
        payroll.setWorking_period(workingPeriod);
        payroll.setStatus(0);
        payroll.setEmployee(employee);
        payroll.setTotalSalary(calculateMonthlySalary(employeeId));

        // Lưu đối tượng Payroll vào cơ sở dữ liệu
        return payrollRepository.save(payroll);
    }
    @Override
    public Double calculateMonthlySalary(Long employeeId) {
        // Lấy thông tin nhân viên
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Lấy số ca làm việc trong tháng hiện tại
        Long timeLate = attendanceService.countHoursLateAttendanceByEmployee(employeeId);
        Long timeEarly = attendanceService.countHoursEarlyAttendanceByEmployee(employeeId);
        Double workShiftsCountHours = attendanceService.countHoursAttendanceByEmployee(employeeId);

        // Tính lương
        WageConfiguration wageConfiguration = wageConfigurationRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Setting not found"));

        Double basicSalary = employee.getBasicSalary();
        Double punishLate = abs((timeLate/60.0))>=wageConfiguration.getHours()?wageConfiguration.getLatePenalty():0;
        Double punishEarly = (timeEarly/60.0)>wageConfiguration.getHours()?wageConfiguration.getLatePenalty():0;
        System.out.println("Phat tre " + punishLate);
        System.out.println("Phat som " + timeEarly);


        Double monthlySalary = basicSalary * workShiftsCountHours - punishLate - punishEarly;

        return monthlySalary;
    }

    @Override
    public Payroll getPayrollById(Long id) {
        Optional<Payroll> payroll = payrollRepository.findById(id);
        return payroll.orElse(null);
    }

    @Override
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    @Override
    public boolean updatePayroll(Long id, Payroll payrollDetails) {
        Optional<Payroll> existingPayrollOpt = payrollRepository.findById(id);
        if (existingPayrollOpt.isPresent()) {
            Payroll existingPayroll = existingPayrollOpt.get();
            existingPayroll.setWorkingDays(payrollDetails.getWorkingDays());
            existingPayroll.setBonus(payrollDetails.getBonus());
            existingPayroll.setTotalIncome(payrollDetails.getTotalIncome());
            existingPayroll.setDeduction(payrollDetails.getDeduction());
            existingPayroll.setTotalSalary(payrollDetails.getTotalSalary());
            existingPayroll.setStatus(payrollDetails.getStatus());
            existingPayroll.setEmployee(payrollDetails.getEmployee());
            payrollRepository.save(existingPayroll);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePayroll(Long id) {
        if (payrollRepository.existsById(id)) {
            payrollRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
