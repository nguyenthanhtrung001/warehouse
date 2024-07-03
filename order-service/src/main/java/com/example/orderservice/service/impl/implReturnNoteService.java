package com.example.orderservice.service.impl;

import com.example.orderservice.entity.ReturnNote;
import com.example.orderservice.repository.ReturnNoteRepository;
import com.example.orderservice.service.IReturnNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implReturnNoteService implements IReturnNoteService {
    @Autowired
    private ReturnNoteRepository returnNoteRepository;

    @Override
    public ReturnNote createReturnNote(ReturnNote returnNote) {
        return returnNoteRepository.save(returnNote);
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
