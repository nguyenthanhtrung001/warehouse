package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.dto.InventoryCheckSlipRequest;
import com.example.inventoryservice.entity.InventoryCheckDetail;
import com.example.inventoryservice.entity.InventoryCheckSlip;
import com.example.inventoryservice.repository.InventoryCheckDetailRepository;
import com.example.inventoryservice.repository.InventoryCheckSlipRepository;
import com.example.inventoryservice.service.IInventoryCheckSlipService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class implInventoryCheckSlipService implements IInventoryCheckSlipService {

    @Autowired
    private InventoryCheckSlipRepository inventoryCheckSlipRepository;

    @Autowired
    private InventoryCheckDetailRepository inventoryCheckDetailRepository;

    @Transactional
    public InventoryCheckSlip createInventoryCheckSlip(InventoryCheckSlipRequest inventoryCheckSlipRequest) {
        InventoryCheckSlip inventoryCheckSlip = new InventoryCheckSlip();
        // Lấy ngày giờ hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi thành Date
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // Đặt ngày giờ hiện tại cho thuộc tính inventoryCheckTime của inventoryCheckSlip
        inventoryCheckSlip.setInventoryCheckTime(currentDate);
        inventoryCheckSlip.setStatus(1);
        // gọi api để cân bằng kho, cập nhật kho
        // kiểm tra nếu ngày cân bằng khác null
       // inventoryCheckSlip.setInventoryBalancingDate(inventoryCheckSlipRequest.getInventoryBalancingDate());

        inventoryCheckSlip.setTotalDiscrepancy(inventoryCheckSlipRequest.getTotalDiscrepancy());
        inventoryCheckSlip.setQuantityDiscrepancyIncrease(inventoryCheckSlipRequest.getQuantityDiscrepancyIncrease());
        inventoryCheckSlip.setQuantityDiscrepancyDecrease(inventoryCheckSlipRequest.getQuantityDiscrepancyDecrease());
        inventoryCheckSlip.setNotes(inventoryCheckSlipRequest.getNotes());
        inventoryCheckSlip.setEmployeeId(inventoryCheckSlipRequest.getEmployeeId());
        try {
            inventoryCheckSlipRepository.save(inventoryCheckSlip);

        }catch (Exception e){
            e.printStackTrace();
        }
        InventoryCheckSlip savedSlip = inventoryCheckSlipRepository.save(inventoryCheckSlip);

        for (var detailRequest : inventoryCheckSlipRequest.getInventoryCheckDetails()) {
            InventoryCheckDetail detail = new InventoryCheckDetail();

            detail.setInventoryCheckSlip(savedSlip);
            detail.setBatchDetail(detailRequest.getBatchDetail());
            detail.setInventory(detailRequest.getInventory());
            detail.setActualQuantity(detailRequest.getActualQuantity());
            detail.setQuantityDiscrepancy(detailRequest.getQuantityDiscrepancy());
            inventoryCheckDetailRepository.save(detail);



        }

        return savedSlip;
    }

    @Override
    public InventoryCheckSlip getInventoryCheckSlipById(Long id) {
        return inventoryCheckSlipRepository.findById(id).orElse(null);
    }

    @Override
    public List<InventoryCheckSlip> getAllInventoryCheckSlips() {
        return inventoryCheckSlipRepository.findAll();
    }


    @Transactional
    public InventoryCheckSlip updateInventoryCheckSlip(Long id, InventoryCheckSlipRequest inventoryCheckSlipRequest) {
        InventoryCheckSlip inventoryCheckSlip = inventoryCheckSlipRepository.findById(id).orElse(null);
        if (inventoryCheckSlip != null) {
            inventoryCheckSlip.setInventoryBalancingDate(inventoryCheckSlipRequest.getInventoryBalancingDate());
            inventoryCheckSlip.setStatus(inventoryCheckSlipRequest.getStatus());
            inventoryCheckSlip.setTotalDiscrepancy(inventoryCheckSlipRequest.getTotalDiscrepancy());
            inventoryCheckSlip.setQuantityDiscrepancyIncrease(inventoryCheckSlipRequest.getQuantityDiscrepancyIncrease());
            inventoryCheckSlip.setQuantityDiscrepancyDecrease(inventoryCheckSlipRequest.getQuantityDiscrepancyDecrease());
            inventoryCheckSlip.setNotes(inventoryCheckSlipRequest.getNotes());


            inventoryCheckSlipRepository.save(inventoryCheckSlip);

            inventoryCheckDetailRepository.deleteAllByInventoryCheckSlipId(id);

            for (var detailRequest : inventoryCheckSlipRequest.getInventoryCheckDetails()) {
                InventoryCheckDetail detail = new InventoryCheckDetail();
                detail.setInventoryCheckSlip(inventoryCheckSlip);
                detail.setBatchDetail(detailRequest.getBatchDetail());
                detail.setInventory(detailRequest.getInventory());
                detail.setActualQuantity(detailRequest.getActualQuantity());
                detail.setQuantityDiscrepancy(detailRequest.getQuantityDiscrepancy());

                inventoryCheckDetailRepository.save(detail);
            }

            return inventoryCheckSlip;
        }
        return null;
    }

    @Override
    public boolean deleteInventoryCheckSlip(Long id) {
        if (inventoryCheckSlipRepository.existsById(id)) {
            inventoryCheckSlipRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
