package com.example.orderservice.service.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.response.InvoiceDetailResponse;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.ReturnDetail;
import com.example.orderservice.repository.ReturnDetailRepository;
import com.example.orderservice.service.IReturnDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implReturnDetailService implements IReturnDetailService {
    @Autowired
    private  ReturnDetailRepository returnDetailRepository;
    @Autowired
    private ProductClient productClient;
    private ModelMapper modelMapper  = new ModelMapper();

    @Override
    public ReturnDetail createReturnDetail(ReturnDetail returnDetail) {
        return returnDetailRepository.save(returnDetail);
    }

    @Override
    public ReturnDetail getReturnDetailById(Long id) {
        Optional<ReturnDetail> returnDetail = returnDetailRepository.findById(id);
        return returnDetail.orElse(null);
    }

    @Override
    public List<InvoiceDetailResponse> getReturnDetailsByInvoiceId(Long invoiceId) {
        List<InvoiceDetailResponse> invoiceDetailResponses = new ArrayList<>();
        List<ReturnDetail>  returnDetails = returnDetailRepository.findByInvoiceId(invoiceId);
        for (ReturnDetail detail: returnDetails){
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
    public List<ReturnDetail> getAllReturnDetails() {
        return returnDetailRepository.findAll();
    }

    @Override
    public boolean updateReturnDetail(Long id, ReturnDetail returnDetailDetails) {
        Optional<ReturnDetail> existingReturnDetailOpt = returnDetailRepository.findById(id);
        if (existingReturnDetailOpt.isPresent()) {
            ReturnDetail existingReturnDetail = existingReturnDetailOpt.get();
            existingReturnDetail.setReturnNote(returnDetailDetails.getReturnNote());
            existingReturnDetail.setProductId(returnDetailDetails.getProductId());
            existingReturnDetail.setQuantity(returnDetailDetails.getQuantity());
            returnDetailRepository.save(existingReturnDetail);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReturnDetail(Long id) {
        if (returnDetailRepository.existsById(id)) {
            returnDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ProductQuantity> getProductQuantitiesForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        return returnDetailRepository.findProductQuantitiesForMonth(startOfMonth, endOfMonth);

    }

    @Override
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = specifiedMonth.atDay(1);
        LocalDate endOfMonth = specifiedMonth.atEndOfMonth();

        return returnDetailRepository.findProductQuantitiesForMonth(startOfMonth, endOfMonth);

    }
}
