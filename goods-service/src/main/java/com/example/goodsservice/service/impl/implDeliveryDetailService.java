package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.client.ProductClient;
import com.example.goodsservice.dto.BathRequest;
import com.example.goodsservice.dto.response.NoteDetailResponse;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.entity.DeliveryDetail;
import com.example.goodsservice.repository.DeliveryDetailRepository;
import com.example.goodsservice.service.IDeliveryDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class implDeliveryDetailService implements IDeliveryDetailService {
     @Autowired
     DeliveryDetailRepository deliveryDetailRepository;
    @Autowired
    ProductClient productClient;
    @Autowired
    InventoryClient inventoryClient;
    private ModelMapper modelMapper  = new ModelMapper();

    @Override
    public boolean deleteDeliveryDetail(Long id) {
        if (deliveryDetailRepository.existsById(id)) {
            deliveryDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<NoteDetailResponse> getNoteDetails(Long nodeID) {
        List<DeliveryDetail> receiptDetails= deliveryDetailRepository.findByDeliveryNoteId(nodeID);

        List<NoteDetailResponse> noteDetailResponses = new ArrayList<>();
        for (DeliveryDetail detail : receiptDetails){
            NoteDetailResponse response = modelMapper.map(detail,NoteDetailResponse.class);
            try{
                String name = productClient.getNameProductByID(detail.getProductId());

                response.setNameProduct(name);
                BathRequest bathRequest = inventoryClient.getBathByDetail(detail.getBatchDetail_Id());
                response.setBath(bathRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
            noteDetailResponses.add(response);
        }
        return noteDetailResponses;
    }
    // not fix
    public List<ProductQuantity> getProductQuantitiesForCurrentMonthAndType(int type) {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return deliveryDetailRepository.findProductQuantitiesForMonthAndType(startOfMonth, endOfMonth, type);
    }
    // hệ thống
    public List<ProductQuantity> getProductQuantitiesForMonthYearAndType(int month, int year, int type) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return deliveryDetailRepository.findProductQuantitiesForMonthAndType(startOfMonth, endOfMonth, type);
    }
    @Override
    public List<ProductQuantity> getProductQuantitiesForMonthYearAndType(int month, int year, int type, Long warehouseId) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return deliveryDetailRepository.findProductQuantitiesForMonthAndType(startOfMonth, endOfMonth, type, warehouseId);
    }

    // not fix
    @Override
    public Integer getTotalQuantity(Long receiptId, Long batchDetailId) {
        Integer totalQuantity = deliveryDetailRepository.findTotalQuantityByReceiptIdAndBatchDetailId(receiptId, batchDetailId);
        return (totalQuantity != null) ? totalQuantity : 0;
    }
    // not fix
    @Override
    public Integer getTotalQuantityByReceiptId(Long receiptId) {
        Integer totalQuantity = deliveryDetailRepository.findTotalQuantityByReceiptId(receiptId);
        return (totalQuantity != null) ? totalQuantity : 0;

    }


}