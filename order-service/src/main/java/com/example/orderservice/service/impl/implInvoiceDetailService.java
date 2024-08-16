package com.example.orderservice.service.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.response.InvoiceDetailResponse;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.InvoiceDetail;
import com.example.orderservice.repository.InvoiceDetailRepository;

import com.example.orderservice.service.IInvoiceDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implInvoiceDetailService implements IInvoiceDetailService {

    @Autowired
    private  InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private ProductClient productClient;

    private ModelMapper modelMapper  = new ModelMapper();
    @Override
    public InvoiceDetail createInvoiceDetail(InvoiceDetail invoiceDetail) {
        return invoiceDetailRepository.save(invoiceDetail);
    }

    @Override
    public InvoiceDetail getInvoiceDetailById(Long id) {
        Optional<InvoiceDetail> invoiceDetail = invoiceDetailRepository.findById(id);
        return invoiceDetail.orElse(null);
    }

    @Override
    public List<InvoiceDetail> getAllInvoiceDetails() {
        return invoiceDetailRepository.findAll();
    }

    @Override
    public List<InvoiceDetailResponse> getInvoiceDetailsByInvoiceId(Long invoiceId) {
        List<InvoiceDetailResponse> invoiceDetailResponses = new ArrayList<>();
        List<InvoiceDetail>invoiceDetails= invoiceDetailRepository.findByInvoiceId_Id(invoiceId);
        for (InvoiceDetail detail: invoiceDetails){
            InvoiceDetailResponse response = modelMapper.map(detail,InvoiceDetailResponse.class);
            try{
                String name = productClient.getNameProductByID(detail.getProductId());
                response.setNameProduct(name);
            }catch (Exception e){
                e.printStackTrace();
                response.setNameProduct("Lỗi Server");
            }
            invoiceDetailResponses.add(response);
        }
        return invoiceDetailResponses;


    }

    @Override
    public List<InvoiceDetail> getDetailsByInvoiceId(Long invoiceId) {

        List<InvoiceDetail>invoiceDetails= invoiceDetailRepository.findByInvoiceId_Id(invoiceId);

        return invoiceDetails;
    }

    @Override
    public boolean updateInvoiceDetail(Long id, InvoiceDetail invoiceDetailDetails) {
        Optional<InvoiceDetail> existingInvoiceDetailOpt = invoiceDetailRepository.findById(id);
        if (existingInvoiceDetailOpt.isPresent()) {
            InvoiceDetail existingInvoiceDetail = existingInvoiceDetailOpt.get();
            existingInvoiceDetail.setProductId(invoiceDetailDetails.getProductId());
            existingInvoiceDetail.setInvoiceId(invoiceDetailDetails.getInvoiceId());
            existingInvoiceDetail.setQuantity(invoiceDetailDetails.getQuantity());
            invoiceDetailRepository.save(existingInvoiceDetail);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteInvoiceDetail(Long id) {
        if (invoiceDetailRepository.existsById(id)) {
            invoiceDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ProductQuantity> getProductQuantities(Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return invoiceDetailRepository.findProductQuantities(pageable);
    }

    @Override
    public List<ProductQuantity> getProductQuantitiesForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return invoiceDetailRepository.findProductQuantitiesForMonth(startOfMonth, endOfMonth);

    }

    @Override
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return invoiceDetailRepository.findProductQuantitiesForMonth(startOfMonth, endOfMonth);
    }
    @Override
    public ProductQuantity getProductQuantitiesForLastThreeMonths(Long productId) {
        ProductQuantity allQuantities = new ProductQuantity();
        allQuantities.setProductId(productId);
        allQuantities.setQuantity(0L);

        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.of(now.getYear(), now.getMonthValue());

        for (int i = 0; i < 3; i++) {
            YearMonth specifiedMonth = currentMonth.minusMonths(i);
            LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

            ProductQuantity quantitiesForMonth = invoiceDetailRepository.findProductQuantityForMonthAndProduct(startOfMonth, endOfMonth, productId);
            if (quantitiesForMonth != null) {
                allQuantities.setQuantity(allQuantities.getQuantity() + quantitiesForMonth.getQuantity());
            }
        }

        return allQuantities;
    }
    @Override
    public String getNoteReturnByInvoiceIdAndProductId(Long invoiceId, Long productId) {
        return invoiceDetailRepository.findNoteReturnByInvoiceIdAndProductId(invoiceId, productId);
    }
}
