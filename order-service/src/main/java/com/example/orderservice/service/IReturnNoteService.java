package com.example.orderservice.service;

import com.example.orderservice.dto.ReturnNoteRequest;
import com.example.orderservice.entity.ReturnNote;

import java.util.List;

public interface  IReturnNoteService {
    ReturnNote createReturnNote(ReturnNote returnNote);
    public ReturnNote createReturnNoteAndDetails(ReturnNoteRequest returnNoteRequest);

    ReturnNote getReturnNoteById(Long id);

    List<ReturnNote> getAllReturnNotes();

    boolean updateReturnNote(Long id, ReturnNote returnNote);

    boolean deleteReturnNote(Long id);
}
