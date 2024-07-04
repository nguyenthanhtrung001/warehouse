package com.example.orderservice.service.impl;

import com.example.orderservice.dto.ReturnDetailRequest;
import com.example.orderservice.dto.ReturnNoteRequest;
import com.example.orderservice.entity.Invoice;
import com.example.orderservice.entity.ReturnDetail;
import com.example.orderservice.entity.ReturnNote;
import com.example.orderservice.repository.InvoiceRepository;
import com.example.orderservice.repository.ReturnDetailRepository;
import com.example.orderservice.repository.ReturnNoteRepository;
import com.example.orderservice.service.IReturnNoteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implReturnNoteService implements IReturnNoteService {
    @Autowired
    private ReturnNoteRepository returnNoteRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ReturnDetailRepository returnDetailRepository;

    @Override
    public ReturnNote createReturnNote(ReturnNote returnNote) {
        return returnNoteRepository.save(returnNote);
    }

    @Transactional
    public ReturnNote createReturnNoteAndDetails(ReturnNoteRequest returnNoteRequest) {
        // Find the associated invoice
        Invoice invoice = invoiceRepository.findById(returnNoteRequest.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        // Create a new ReturnNote
        ReturnNote returnNote = new ReturnNote();

        returnNote.setReturnDate(LocalDate.now());
        returnNote.setInvoice(invoice);
        returnNote.setEmployeeId(returnNoteRequest.getEmployeeId());
        returnNote.setStatus(returnNoteRequest.getStatus());

        // Save the ReturnNote to get the generated ID
        ReturnNote savedReturnNote = returnNoteRepository.save(returnNote);

        // Create and save ReturnDetails
        List<ReturnDetail> returnDetails = new ArrayList<>();
        for (ReturnDetailRequest detailRequest : returnNoteRequest.getReturnDetails()) {
            ReturnDetail returnDetail = new ReturnDetail();
            returnDetail.setReturnNote(savedReturnNote.getId());
            returnDetail.setProductId(detailRequest.getProductId());
            returnDetail.setQuantity(detailRequest.getQuantity());
            returnDetails.add(returnDetail);
        }

        returnDetailRepository.saveAll(returnDetails);

        return savedReturnNote;
    }

    @Override
    public ReturnNote getReturnNoteById(Long id) {
        Optional<ReturnNote> returnNote = returnNoteRepository.findById(id);
        return returnNote.orElse(null);
    }

    @Override
    public List<ReturnNote> getAllReturnNotes() {
        return returnNoteRepository.findAll();
    }

    @Override
    public boolean updateReturnNote(Long id, ReturnNote returnNoteDetails) {
        Optional<ReturnNote> existingReturnNoteOpt = returnNoteRepository.findById(id);
        if (existingReturnNoteOpt.isPresent()) {
            ReturnNote existingReturnNote = existingReturnNoteOpt.get();
            existingReturnNote.setReturnDate(returnNoteDetails.getReturnDate());
            existingReturnNote.setInvoice(returnNoteDetails.getInvoice());
            existingReturnNote.setEmployeeId(returnNoteDetails.getEmployeeId());
            existingReturnNote.setStatus(returnNoteDetails.getStatus());
            returnNoteRepository.save(existingReturnNote);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReturnNote(Long id) {
        if (returnNoteRepository.existsById(id)) {
            returnNoteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
