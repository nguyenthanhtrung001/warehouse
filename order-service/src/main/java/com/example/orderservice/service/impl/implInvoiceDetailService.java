package com.example.orderservice.service.impl;

import com.example.orderservice.entity.InvoiceDetail;
import com.example.orderservice.repository.InvoiceDetailRepository;

import com.example.orderservice.service.IInvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implInvoiceDetailService implements IInvoiceDetailService {

    @Autowired
    private  InvoiceDetailRepository invoiceDetailRepository;


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
}
