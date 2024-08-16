package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.response.PayrollSummaryResponse;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.entity.Payroll;
import com.example.employeeservice.entity.WageConfiguration;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.repository.PayrollRepository;
import com.example.employeeservice.repository.WageConfigurationRepository;
import com.example.employeeservice.service.IAttendanceService;
import com.example.employeeservice.service.IEmployeeService;
import com.example.employeeservice.service.IPayrollService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

@Service
public class implPayrollService implements IPayrollService {
    String reason = "";
    Double salary = 0.0;
    Double punish = 0.0;

    @Autowired
    private  PayrollRepository payrollRepository;
    @Autowired
    private WageConfigurationRepository wageConfigurationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private IAttendanceService attendanceService;
    @Autowired
    private IEmployeeService employeeService;


    @Override
    public Payroll createPayroll(Payroll payroll) {
        return payrollRepository.save(payroll);
    }

    @Transactional
    public List<Payroll> createPayrollForAllEmployee() {

        // Tạo khoảng thời gian làm việc
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        List<Employee> employees = employeeService.getAllEmployees();
        String workingPeriod = firstDayOfMonth.format(formatter) + " - " + lastDayOfMonth.format(formatter);
        List<Payroll> payrolls = new ArrayList<>();

        for ( Employee employee : employees){
            Payroll payroll = findPayrollsByWorkingPeriodAndEmployeeId(workingPeriod, employee.getId());
            if (payroll == null){
                Payroll pay = createPayrollByEmployee(employee.getId());
                payrolls.add(pay);
            }
            else {
                deletePayroll(payroll.getId());
                Payroll pay = createPayrollByEmployee(employee.getId());
                payrolls.add(pay);
            }
        }
        return payrolls;
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
        payroll.setTotalIncome(salary);
        payroll.setDeduction(punish);
        payroll.setNote(reason);
try {
    payrollRepository.save(payroll);
}catch (Exception e){
    e.printStackTrace();
}
        // Lưu đối tượng Payroll vào cơ sở dữ liệu
        return payrollRepository.save(payroll);
    }
    @Override
    public Double calculateMonthlySalary(Long employeeId) {
        // Lấy thông tin nhân viên
        reason = "";
        salary = 0.0;
        punish = 0.0;

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
        Double punishEarly = abs(timeEarly/60.0)>=wageConfiguration.getHours()?wageConfiguration.getEarlyPenalty():0;
        System.out.println("Phat tre " + punishLate);
        System.out.println("Phat som " + abs(timeEarly/60.0));
        reason = "Phạt trể: "+timeLate/60.0*(-1)+"h"+
                "- về sớm: "+ timeEarly/60.0*(-1)+"h";
        salary = basicSalary * workShiftsCountHours;
        punish = punishLate + punishEarly;
        Double monthlySalary =salary  - punish;

        return monthlySalary;
    }

    @Override
    public Payroll getPayrollById(Long id) {
        Optional<Payroll> payroll = payrollRepository.findById(id);
        return payroll.orElse(null);
    }

    @Override
    public Payroll updatePayroll(Payroll payroll) {

       return  payrollRepository.save(payroll);
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
            existingPayroll.setStatus(2);
            existingPayroll.setEmployee(payrollDetails.getEmployee());
            existingPayroll.setNote(payrollDetails.getNote());
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

    @Override
    public List<Payroll> getPayrollsByWorkingPeriod(String workingPeriod) {
        return payrollRepository.findByWorkingPeriod(workingPeriod);
    }


    @Override
    public List<PayrollSummaryResponse> getPayrollGroupByWorkingPeriodAndStatus() {
        return payrollRepository.findPayrollGroupByWorkingPeriodAndStatus();
    }

    @Override
    public Payroll findPayrollsByWorkingPeriodAndEmployeeId(String workingPeriod, Long employeeId) {
        return payrollRepository.findByWorkingPeriodAndEmployeeId(workingPeriod, employeeId);

    }
}
