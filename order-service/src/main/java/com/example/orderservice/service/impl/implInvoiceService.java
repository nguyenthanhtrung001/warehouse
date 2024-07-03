package com.example.orderservice.service.impl;

import com.example.orderservice.dto.InvoiceDetailRequest;
import com.example.orderservice.dto.InvoiceRequest;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Invoice;
import com.example.orderservice.entity.InvoiceDetail;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.InvoiceDetailRepository;
import com.example.orderservice.repository.InvoiceRepository;
import com.example.orderservice.service.IInvoiceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class implInvoiceService implements IInvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice createInvoice(InvoiceRequest invoiceRequest) {
        // Find customer by ID
        Customer customer = customerRepository.findById(invoiceRequest.getCustomer().getId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Create new Invoice
        // Lấy ngày giờ hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi thành Date
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Invoice invoice = new Invoice();
        invoice.setPrintDate(currentDate); // Set print date to current date
        invoice.setCustomer(customer);
        invoice.setStatus(1); // Assuming status 1 means Success
        invoice.setPrice(calculateTotalPrice(invoiceRequest.getInvoiceDetails())); // Calculate total price

        // Save Invoice to get ID generated
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Create InvoiceDetails
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        for (InvoiceDetailRequest detailRequest : invoiceRequest.getInvoiceDetails()) {
            InvoiceDetail detail = new InvoiceDetail();
            detail.setInvoiceId(savedInvoice.getId());
            detail.setProductId(detailRequest.getProductId());
            detail.setQuantity(detailRequest.getQuantity());
            invoiceDetails.add(detail);
        }

        // Save all InvoiceDetails
        invoiceDetailRepository.saveAll(invoiceDetails);

        return savedInvoice;
    }

    // Helper method to calculate total price based on details
    private long calculateTotalPrice(List<InvoiceDetailRequest> invoiceDetails) {
        long totalPrice = 0;
        for (InvoiceDetailRequest detail : invoiceDetails) {
            totalPrice += detail.getPrice() * detail.getQuantity();
        }
        return totalPrice;
    }



    @Override
    public Invoice getInvoiceById(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        return invoice.orElse(null);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public boolean updateInvoice(Long id, Invoice invoiceDetails) {
        Optional<Invoice> existingInvoiceOpt = invoiceRepository.findById(id);
        if (existingInvoiceOpt.isPresent()) {
            Invoice existingInvoice = existingInvoiceOpt.get();
            existingInvoice.setPrice(invoiceDetails.getPrice());
            existingInvoice.setCustomer(invoiceDetails.getCustomer());
            existingInvoice.setStatus(invoiceDetails.getStatus());
            invoiceRepository.save(existingInvoice);
            return true;
        }
        return false;
    }

    @Override
    public Invoice updateInvoice(Long invoiceId, InvoiceRequest invoiceRequest) {
        // Find existing Invoice by ID
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        if (!optionalInvoice.isPresent()) {
            throw new IllegalArgumentException("Invoice not found");
        }
        Invoice existingInvoice = optionalInvoice.get();

        // Update customer (if changed)
        Customer customer = customerRepository.findById(invoiceRequest.getCustomer().getId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        existingInvoice.setCustomer(customer);

        // Update price (if needed)
        existingInvoice.setPrice(calculateTotalPrice(invoiceRequest.getInvoiceDetails()));

        // Update InvoiceDetails
        List<InvoiceDetail> updatedInvoiceDetails = new ArrayList<>();
        for (InvoiceDetailRequest detailRequest : invoiceRequest.getInvoiceDetails()) {
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setInvoiceId(existingInvoice.getId());
            invoiceDetail.setProductId(detailRequest.getProductId());
            invoiceDetail.setQuantity(detailRequest.getQuantity());
            updatedInvoiceDetails.add(invoiceDetail);
        }

        // Delete existing InvoiceDetails and save updated ones
        invoiceDetailRepository.deleteByInvoiceId(invoiceId);
        invoiceDetailRepository.saveAll(updatedInvoiceDetails);

        // Save and return updated Invoice
        return invoiceRepository.save(existingInvoice);
    }
    @Override
    public boolean deleteInvoice(Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
