package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.dto.InventoryCheckDetailRequest;
import com.example.inventoryservice.dto.InventoryCheckSlipRequest;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.InventoryCheckDetail;
import com.example.inventoryservice.entity.InventoryCheckSlip;
import com.example.inventoryservice.mapper.CheckSlipMapper;
import com.example.inventoryservice.repository.BatchDetailRepository;
import com.example.inventoryservice.repository.InventoryCheckDetailRepository;
import com.example.inventoryservice.repository.InventoryCheckSlipRepository;
import com.example.inventoryservice.service.IBatchDetailService;
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
    @Autowired
    private IBatchDetailService batchDetailService;


    @Transactional
    public InventoryCheckSlip createInventoryCheckSlip(InventoryCheckSlipRequest inventoryCheckSlipRequest) {
        InventoryCheckSlip inventoryCheckSlip = new InventoryCheckSlip();

        // Lấy ngày giờ hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        inventoryCheckSlip.setInventoryCheckTime(currentDateTime);
        inventoryCheckSlip.setStatus(1);

        // gọi api để cân bằng kho, cập nhật kho

        inventoryCheckSlip.setTotalDiscrepancy(inventoryCheckSlipRequest.getTotalDiscrepancy());
        inventoryCheckSlip.setQuantityDiscrepancyIncrease(inventoryCheckSlipRequest.getQuantityDiscrepancyIncrease());
        inventoryCheckSlip.setQuantityDiscrepancyDecrease(inventoryCheckSlipRequest.getQuantityDiscrepancyDecrease());
        inventoryCheckSlip.setNotes(inventoryCheckSlipRequest.getNotes());
        inventoryCheckSlip.setEmployeeId(inventoryCheckSlipRequest.getEmployeeId());

        InventoryCheckSlip savedSlip = inventoryCheckSlipRepository.save(inventoryCheckSlip);

        for (InventoryCheckDetailRequest detailRequest : inventoryCheckSlipRequest.getInventoryCheckDetails()) {
            InventoryCheckDetail detail = new InventoryCheckDetail();

            detail.setInventoryCheckSlip(savedSlip);

            BatchDetail batchDetail = new BatchDetail(detailRequest.getBatchDetail());
            detail.setBatchDetail(batchDetail);

            detail.setInventory(detailRequest.getInventory());
            detail.setActualQuantity(detailRequest.getActualQuantity());
            detail.setQuantityDiscrepancy(detailRequest.getQuantityDiscrepancy());


            batchDetailService.updateQuantityForCheckInventory(detailRequest.getBatchDetail(), detail.getQuantityDiscrepancy());

            inventoryCheckDetailRepository.save(detail);

        }

         inventoryCheckSlip.setInventoryBalancingDate(LocalDateTime.now());

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

            for (InventoryCheckDetailRequest detailRequest : inventoryCheckSlipRequest.getInventoryCheckDetails()) {
                InventoryCheckDetail detail = new InventoryCheckDetail();
                detail.setInventoryCheckSlip(inventoryCheckSlip);
              /*  detail.setBatchDetail(detailRequest.getBatchDetail());*/
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
