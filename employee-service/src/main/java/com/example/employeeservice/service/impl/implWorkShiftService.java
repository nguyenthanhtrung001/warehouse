package com.example.employeeservice.service.impl;

import com.example.employeeservice.entity.WorkShift;
import com.example.employeeservice.repository.WorkShiftRepository;
import com.example.employeeservice.service.IWorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implWorkShiftService implements IWorkShiftService {
    @Autowired
    private  WorkShiftRepository workShiftRepository;

    @Override
    public WorkShift createWorkShift(WorkShift workShift) {
        return workShiftRepository.save(workShift);
    }

    @Override
    public WorkShift getWorkShiftById(Long id) {
        Optional<WorkShift> workShift = workShiftRepository.findById(id);
        return workShift.orElse(null);
    }

    @Override
    public List<WorkShift> getAllWorkShifts() {
        return workShiftRepository.findAll();
    }

    @Override
    public boolean updateWorkShift(Long id, WorkShift workShiftDetails) {
        Optional<WorkShift> existingWorkShiftOpt = workShiftRepository.findById(id);
        if (existingWorkShiftOpt.isPresent()) {
            WorkShift existingWorkShift = existingWorkShiftOpt.get();
            existingWorkShift.setShiftName(workShiftDetails.getShiftName());
            existingWorkShift.setStartTime(workShiftDetails.getStartTime());
            existingWorkShift.setEndTime(workShiftDetails.getEndTime());
            existingWorkShift.setStatus(workShiftDetails.getStatus());
            workShiftRepository.save(existingWorkShift);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteWorkShift(Long id) {
        if (workShiftRepository.existsById(id)) {
            workShiftRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
