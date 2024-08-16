package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.dto.InventoryCheckDetailRequest;
import com.example.inventoryservice.dto.InventoryCheckSlipRequest;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.InventoryCheckDetail;
import com.example.inventoryservice.entity.InventoryCheckSlip;
import com.example.inventoryservice.repository.InventoryCheckDetailRepository;
import com.example.inventoryservice.repository.InventoryCheckSlipRepository;
import com.example.inventoryservice.service.IBatchDetailService;
import com.example.inventoryservice.service.IInventoryCheckSlipService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static java.lang.Math.abs;

@Service
@RequiredArgsConstructor
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

        inventoryCheckSlip.setNotes(inventoryCheckSlipRequest.getNotes());
        inventoryCheckSlip.setEmployeeId(inventoryCheckSlipRequest.getEmployeeId());
        try{
            InventoryCheckSlip savedSlip = inventoryCheckSlipRepository.save(inventoryCheckSlip);

        }catch (Exception e){
            e.printStackTrace();
        }
        InventoryCheckSlip savedSlip = inventoryCheckSlipRepository.save(inventoryCheckSlip);
        int totalIncre = 0;
        int totalDecre = 0;
        for (InventoryCheckDetailRequest detailRequest : inventoryCheckSlipRequest.getInventoryCheckDetails()) {

            InventoryCheckDetail detail = new InventoryCheckDetail();

            int total = detailRequest.getActualQuantity() - detailRequest.getInventory();

            if (total > 0) totalIncre += total;
            else if (total < 0 ) totalDecre += total;

            savedSlip= inventoryCheckSlipRepository.save(inventoryCheckSlip);

            detail.setInventoryCheckSlip(savedSlip);

            BatchDetail batchDetail = new BatchDetail(detailRequest.getBatchDetail());
            detail.setBatchDetail(batchDetail);
            detail.setInventory(detailRequest.getInventory());
            detail.setActualQuantity(detailRequest.getActualQuantity());
            detail.setQuantityDiscrepancy(detailRequest.getActualQuantity() - detailRequest.getInventory());


            batchDetailService.updateQuantityForCheckInventory(detailRequest.getBatchDetail(), detail.getQuantityDiscrepancy());

            inventoryCheckDetailRepository.save(detail);

        }
        inventoryCheckSlip.setTotalDiscrepancy(totalDecre + totalIncre);
        inventoryCheckSlip.setQuantityDiscrepancyDecrease(totalDecre);
        inventoryCheckSlip.setQuantityDiscrepancyIncrease(totalIncre);
        inventoryCheckSlip.setInventoryBalancingDate(LocalDateTime.now());

        return savedSlip;
    }

    @Override
    public InventoryCheckSlip getInventoryCheckSlipById(Long id) {
        return inventoryCheckSlipRepository.findById(id).orElse(null);
    }

    @Override
    public List<InventoryCheckDetail> getDetailCheckSlipById(Long id) {
        return inventoryCheckDetailRepository.findByInventoryCheckSlipId(id);

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
        InventoryCheckSlip deliveryNote = inventoryCheckSlipRepository.findById(id).orElse(null);
        if (deliveryNote.getStatus() != 1) return false;
        List<InventoryCheckDetail> inventoryCheckDetails = getDetailCheckSlipById(id);
        for (InventoryCheckDetail detail : inventoryCheckDetails) {
            Integer quantity = detail.getQuantityDiscrepancy()*-1;
            batchDetailService.updateQuantityForCheckInventory(detail.getBatchDetail().getId(),quantity);
            inventoryCheckDetailRepository.deleteById(detail.getId());
        }
        inventoryCheckSlipRepository.deleteById(id);
        return true;
    }

    @Override
    public List<ProductQuantity> getProductDiscrepanciesForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return inventoryCheckDetailRepository.findProductDiscrepanciesForMonth(startOfMonth, endOfMonth);

    }

    @Override
    public List<ProductQuantity> getProductDiscrepanciesForMonthYear(int month, int year) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return inventoryCheckDetailRepository.findProductDiscrepanciesForMonth(startOfMonth, endOfMonth);

    }

    @Override
    public List<ProductQuantity> getProductDiscrepanciesLessForMonthYear(int month, int year) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return inventoryCheckDetailRepository.findProductDiscrepanciesLessForMonth(startOfMonth, endOfMonth);

    }

    @Override
    public List<ProductQuantity> getProductDiscrepanciesLessForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return inventoryCheckDetailRepository.findProductDiscrepanciesLessForMonth(startOfMonth, endOfMonth);

    }

}
