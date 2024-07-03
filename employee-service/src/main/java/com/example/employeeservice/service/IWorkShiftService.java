package com.example.employeeservice.service;

import com.example.employeeservice.entity.WorkShift;

import java.util.List;

public interface IWorkShiftService{
    WorkShift createWorkShift(WorkShift workShift);

    WorkShift getWorkShiftById(Long id);

    List<WorkShift> getAllWorkShifts();

    boolean updateWorkShift(Long id, WorkShift workShift);

    boolean deleteWorkShift(Long id);
}
