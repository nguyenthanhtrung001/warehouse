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
import java.util.Optional;

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
    public DeliveryDetail createDeliveryDetail(DeliveryDetail deliveryDetail) {
        return deliveryDetailRepository.save(deliveryDetail);
    }

    @Override
    public Optional<DeliveryDetail> getDeliveryDetailById(Long id) {
        return deliveryDetailRepository.findById(id);
    }

    @Override
    public List<DeliveryDetail> getAllDeliveryDetails() {
        return deliveryDetailRepository.findAllByType(1);
    }

    @Override
    public boolean updateDeliveryDetail(Long id, DeliveryDetail deliveryDetail) {
        if (deliveryDetailRepository.existsById(id)) {
            deliveryDetail.setQuantity(deliveryDetail.getQuantity());
           deliveryDetail.setBatchDetail_Id(deliveryDetail.getBatchDetail_Id());

            deliveryDetailRepository.save(deliveryDetail);
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteDeliveryDetail(Long id) {
        if (deliveryDetailRepository.existsById(id)) {
            deliveryDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<NoteDetailResponse> getNoteDetails(Long node) {
        List<DeliveryDetail> receiptDetails= deliveryDetailRepository.findByDeliveryNoteId(node);

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

    public List<ProductQuantity> getProductQuantitiesForCurrentMonthAndType(int type) {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return deliveryDetailRepository.findProductQuantitiesForMonthAndType(startOfMonth, endOfMonth, type);
    }

    public List<ProductQuantity> getProductQuantitiesForMonthYearAndType(int month, int year, int type) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return deliveryDetailRepository.findProductQuantitiesForMonthAndType(startOfMonth, endOfMonth, type);
    }

    @Override
    public Integer getTotalQuantity(Long receiptId, Long batchDetailId) {
        Integer totalQuantity = deliveryDetailRepository.findTotalQuantityByReceiptIdAndBatchDetailId(receiptId, batchDetailId);
        return (totalQuantity != null) ? totalQuantity : 0;
    }

    @Override
    public Integer getTotalQuantityByReceiptId(Long receiptId) {
        Integer totalQuantity = deliveryDetailRepository.findTotalQuantityByReceiptId(receiptId);
        return (totalQuantity != null) ? totalQuantity : 0;

    }
}