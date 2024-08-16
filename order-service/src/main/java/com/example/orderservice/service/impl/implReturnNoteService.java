package com.example.orderservice.service.impl;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.ReturnDetailRequest;
import com.example.orderservice.dto.ReturnNoteRequest;
import com.example.orderservice.dto.response.OrderQuantity;
import com.example.orderservice.entity.Invoice;
import com.example.orderservice.entity.InvoiceDetail;
import com.example.orderservice.entity.ReturnDetail;
import com.example.orderservice.entity.ReturnNote;
import com.example.orderservice.repository.ReturnDetailRepository;
import com.example.orderservice.repository.ReturnNoteRepository;
import com.example.orderservice.security.EncoderDecoder;
import com.example.orderservice.service.IInvoiceDetailService;
import com.example.orderservice.service.IInvoiceService;
import com.example.orderservice.service.IReturnDetailService;
import com.example.orderservice.service.IReturnNoteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implReturnNoteService implements IReturnNoteService {
    @Autowired
    private ReturnNoteRepository returnNoteRepository;

    @Autowired
    private ReturnDetailRepository returnDetailRepository;
    @Autowired
    private IInvoiceDetailService invoiceDetailService;
    @Autowired
    private IReturnDetailService returnDetailService;
    @Autowired
    InventoryClient inventoryClient;
    @Autowired
    IInvoiceService invoiceService;

    @Override
    public ReturnNote createReturnNote(ReturnNote returnNote) {
        return returnNoteRepository.save(returnNote);
    }

    @Transactional
    public ReturnNote createReturnNoteAndDetails(ReturnNoteRequest returnNoteRequest) {
        // Find the associated invoice
        Invoice invoice = invoiceService.getInvoiceById(returnNoteRequest.getInvoiceId());

        // Create a new ReturnNote
        ReturnNote returnNote = new ReturnNote();
        invoiceService.updateInvoiceStatus(invoice.getId(),3);
        returnNote.setReturnDate(LocalDate.now());

        returnNote.setInvoice(invoice);
        returnNote.setEmployeeId(returnNoteRequest.getEmployeeId());
        returnNote.setStatus(returnNoteRequest.getStatus());
        returnNote.setPrice(returnNoteRequest.getPrice());

        // Save the ReturnNote to get the generated ID
        ReturnNote savedReturnNote = returnNoteRepository.save(returnNote);

        // Create and save ReturnDetails
        List<ReturnDetail> returnDetails = new ArrayList<>();
        for (ReturnDetailRequest detailRequest : returnNoteRequest.getReturnDetails()) {
            ReturnDetail returnDetail = new ReturnDetail();
            returnDetail.setReturnNote(savedReturnNote.getId());
            returnDetail.setPurchasePrice(detailRequest.getPurchasePrice());
            returnDetail.setProductId(detailRequest.getProductId());
            returnDetail.setQuantity(detailRequest.getQuantity());
            returnDetails.add(returnDetail);

            List<OrderQuantity> bathQuantity = EncoderDecoder.decodeFromJsonBase64(detailRequest.getNote());

            int sumQuantityOrder = bathQuantity.stream()
                    .mapToInt(OrderQuantity::getQuantity)
                    .sum();
            int quantityReturnOrder = returnDetail.getQuantity();

            System.out.println("so luong tra:" + sumQuantityOrder);
            // So sánh tổng số lượng trả lại với số lượng trả|| bathQuantity.size() == 1
            if (sumQuantityOrder == quantityReturnOrder ) {
                // Cập nhật lại như cũ
                updateAsBeforeSale_inventory(bathQuantity);
            } else {
                // Cập nhật dữ liệu (chỉ cần lặp qua list 1 lần)
                updateDataReturn_inventory(bathQuantity, quantityReturnOrder);
            }

        }

        returnDetailRepository.saveAll(returnDetails);

        return savedReturnNote;
    }

    private void updateAsBeforeSale_inventory(List<OrderQuantity> bathQuantity) {
        //
        System.out.println("Cap nhat nhu truoc do");
        for (OrderQuantity orderQuantity : bathQuantity) {
            String Update = inventoryClient.updateQuantityForReturnOrder(orderQuantity.getBathDetail_Id(), orderQuantity.getQuantity());
        }
    }

    private void updateDataReturn_inventory(List<OrderQuantity> bathQuantity, Integer quantityReturnOrder) {
        System.out.println("Cap nhat so luong vao 1 lo");
        String Update = inventoryClient.updateQuantityForReturnOrder(bathQuantity.get(0).getBathDetail_Id(), quantityReturnOrder);
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
    public boolean deleteReturnNote(Long invoiceId) {

        ReturnNote returnNote = returnNoteRepository.findByInvoiceId(invoiceId);

        List<ReturnDetail> returnDetails = returnDetailRepository.findByInvoiceId(invoiceId);


        for (ReturnDetail detail : returnDetails) {
            String code = invoiceDetailService.getNoteReturnByInvoiceIdAndProductId(invoiceId, detail.getProductId());
            List<OrderQuantity> bathQuantity = EncoderDecoder.decodeFromJsonBase64(code);
            for (OrderQuantity orderQuantity : bathQuantity) {
                inventoryClient.updateQuantityForReturnOrder(orderQuantity.getBathDetail_Id(), (detail.getQuantity()*-1));
            }

            Boolean tmp = returnDetailService.deleteReturnDetail(detail.getId());
            if (tmp == true) System.out.println("Xoa Thanh cong chi tiet nhap");
        }
        returnNoteRepository.deleteById(returnNote.getId());
        invoiceService.updateInvoiceStatus(invoiceId,2);
        return true;
    }

    @Override
    public long getTotalPriceForCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        List<ReturnNote> returnNotes = returnNoteRepository.findByReturnDateBetween(startOfWeek, endOfWeek);

        return returnNotes.stream()
                .filter(returnNote -> returnNote.getPrice() != null) // Kiểm tra null
                .mapToLong(ReturnNote::getPrice)
                .sum();
    }

    @Override
    public long getTotalPriceForCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        List<ReturnNote> returnNotes = returnNoteRepository.findByReturnDateBetween(startOfMonth, endOfMonth);

        return returnNotes.stream()
                .filter(returnNote -> returnNote.getPrice() != null) // Kiểm tra null
                .mapToLong(ReturnNote::getPrice)
                .sum();
    }

    @Override
    public long countReturnNotesForCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return returnNoteRepository.countByReturnDateBetween(startOfMonth, endOfMonth);

    }

    @Override
    public long calculateRevenueForCurrentMonth() {
        long totalInvoicePrice = invoiceService.getTotalPriceForCurrentMonth();
        long totalReturnNotePrice = getTotalPriceForCurrentMonth();
        System.out.println("GT1"+totalInvoicePrice);
        System.out.println("GT2"+totalReturnNotePrice);
        System.out.println("GT3"+ (totalInvoicePrice - totalReturnNotePrice));
        return totalInvoicePrice - totalReturnNotePrice;

    }
}
