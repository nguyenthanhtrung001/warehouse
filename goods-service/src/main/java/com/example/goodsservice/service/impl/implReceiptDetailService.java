package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.client.ProductClient;
import com.example.goodsservice.dto.BathRequest;
import com.example.goodsservice.dto.response.ProductQuantity;
import com.example.goodsservice.dto.response.ReceiptDetailResponse;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.ReceiptDetail;
import com.example.goodsservice.repository.DeliveryDetailRepository;
import com.example.goodsservice.repository.ReceiptDetailRepository;
import com.example.goodsservice.repository.ReceiptRepository;
import com.example.goodsservice.service.IDeliveryDetailService;
import com.example.goodsservice.service.IReceiptDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implReceiptDetailService implements IReceiptDetailService {
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;
    @Autowired
    private IDeliveryDetailService deliveryDetailService;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    ProductClient productClient;
    @Autowired
    InventoryClient inventoryClient;

    private ModelMapper modelMapper  = new ModelMapper();


    @Override
    public ReceiptDetail createReceiptDetail(ReceiptDetail receiptDetail) {
        return receiptDetailRepository.save(receiptDetail);
    }

    @Override
    public List<ProductQuantity> getProductQuantitiesForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return receiptDetailRepository.findProductQuantitiesForCurrentMonth(startOfMonth, endOfMonth);

    }

    @Override
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return receiptDetailRepository.findProductQuantitiesForCurrentMonth(startOfMonth, endOfMonth);

    }

    @Override
    public ReceiptDetail addReceiptDetail(Long receiptId, ReceiptDetail receiptDetail) {
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow(() -> new RuntimeException("Receipt not found"));
        receiptDetail.setReceipt(receipt);
        return receiptDetailRepository.save(receiptDetail);
    }

    @Override
    public List<ReceiptDetailResponse> getReceiptDetails(Long receiptId) {
        List<ReceiptDetail> receiptDetails= receiptDetailRepository.findByReceiptId(receiptId);

        List<ReceiptDetailResponse> receiptDetailResponses= new ArrayList<>();
        for (ReceiptDetail detail : receiptDetails){
            ReceiptDetailResponse response = modelMapper.map(detail,ReceiptDetailResponse.class);
            try{
                String name = productClient.getNameProductByID(detail.getProductId());
                BathRequest bathRequest = inventoryClient.getBathByDetail(detail.getBatchDetail_Id());
                response.setNameProduct(name);
                response.setBath(bathRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
            receiptDetailResponses.add(response);
        }
        return receiptDetailResponses;
    }
    @Override
    public List<ReceiptDetail> getReceiptDetailsWithReceiptId(Long receiptId) {
        List<ReceiptDetail> receiptDetails= receiptDetailRepository.findByReceiptId(receiptId);
        return receiptDetails;
    }
    @Override
    public List<ReceiptDetailResponse> getReceiptDetailsWithUpdateQuantity(Long receiptId) {
        List<ReceiptDetail> receiptDetails= receiptDetailRepository.findByReceiptId(receiptId);
        System.out.println("hello : ");
        List<ReceiptDetailResponse> receiptDetailResponses= new ArrayList<>();
        for (ReceiptDetail detail : receiptDetails){
            ReceiptDetailResponse response = modelMapper.map(detail,ReceiptDetailResponse.class);
            int quantity = response.getQuantity() -deliveryDetailService.getTotalQuantity(response.getReceipt().getId(),response.getBatchDetail_Id());
            System.out.println("SL tra: "+deliveryDetailService.getTotalQuantity(response.getReceipt().getId(),response.getBatchDetail_Id()));
            System.out.println("SL nhập: "+response.getQuantity());
            System.out.println("SL : "+quantity);


            response.setQuantity(quantity);
            try{
                String name = productClient.getNameProductByID(detail.getProductId());
                BathRequest bathRequest = inventoryClient.getBathByDetail(detail.getBatchDetail_Id());
                response.setNameProduct(name);
                response.setBath(bathRequest);

            }catch (Exception e){
                e.printStackTrace();
            }
            receiptDetailResponses.add(response);
        }
        return receiptDetailResponses;
    }

    @Override
    public Optional<ReceiptDetail> getReceiptDetailById(Long id) {
        return receiptDetailRepository.findById(id);
    }

    @Override
    public List<ReceiptDetail> getAllReceiptDetails() {
        return receiptDetailRepository.findAll();
    }

    @Override
    public boolean updateReceiptDetail(Long id, ReceiptDetail receiptDetail) {
        if (receiptDetailRepository.existsById(id)) {
            receiptDetail.setPurchasePrice(receiptDetail.getPurchasePrice());
            receiptDetail.setQuantity(receiptDetail.getQuantity());
            receiptDetailRepository.save(receiptDetail);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReceiptDetail(Long id) {
        if (receiptDetailRepository.existsById(id)) {
            receiptDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Integer getTotalQuantityByReceiptId(Long receiptId) {
        Integer totalQuantity = receiptDetailRepository.findTotalQuantityByReceiptId(receiptId);
        return (totalQuantity != null) ? totalQuantity : 0;

    }

    @Override
    public boolean existsByProductId(Long productId) {
        return receiptDetailRepository.existsByProductId(productId);
    }
}