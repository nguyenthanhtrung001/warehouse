package com.example.orderservice.service.impl;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.ReturnDetailRequest;
import com.example.orderservice.dto.ReturnNoteRequest;
import com.example.orderservice.dto.response.MonthRevenue;
import com.example.orderservice.dto.response.OrderQuantity;
import com.example.orderservice.entity.Invoice;
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
import java.time.YearMonth;
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
            // thay đỗi quan hệ giữa returnnote và detail
            ReturnNote note = new ReturnNote();
            note.setId(savedReturnNote.getId());
            returnDetail.setReturnNote(note);
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
    public List<ReturnNote> getAllReturnNotes(Long warehouseId) {
        return returnNoteRepository.findByWarehouseId(warehouseId);
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
       return 0;
    }

    @Override
    public long getTotalPriceForCurrentMonth(Long wareHouseId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        List<ReturnNote> returnNotes = returnNoteRepository.findByReturnDateBetweenAndInvoice_WarehouseId(startOfMonth, endOfMonth,wareHouseId);

        return returnNotes.stream()
                .filter(returnNote -> returnNote.getPrice() != null) // Kiểm tra null
                .mapToLong(ReturnNote::getPrice)
                .sum();
    }
    public long getTotalPriceForMonth(int month, int year) {
        // Tạo đối tượng YearMonth với tháng và năm cụ thể
        YearMonth specifiedMonth = YearMonth.of(year, month);

        // Xác định ngày bắt đầu và kết thúc của tháng
        LocalDate startOfMonth = specifiedMonth.atDay(1);
        LocalDate endOfMonth = specifiedMonth.atEndOfMonth();

        // Lấy danh sách các phiếu trả hàng trong khoảng thời gian xác định
        List<ReturnNote> returnNotes = returnNoteRepository.findByReturnDateBetween(startOfMonth, endOfMonth);

        // Tính tổng giá trị của các phiếu trả hàng, bỏ qua các phiếu có giá trị null
        return returnNotes.stream()
                .filter(returnNote -> returnNote.getPrice() != null)
                .mapToLong(ReturnNote::getPrice)
                .sum();
    }

    @Override
    public long countReturnNotesForCurrentMonth(Long wareHouseId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return returnNoteRepository.countByReturnDateBetweenAndInvoice_WarehouseId(startOfMonth, endOfMonth,wareHouseId);

    }

    @Override
    public long calculateRevenueForCurrentMonth(Long wareHouseId) {
        long totalInvoicePrice = invoiceService.getTotalPriceForMonth(wareHouseId);
        long totalReturnNotePrice = getTotalPriceForCurrentMonth(wareHouseId);
        System.out.println("GT1"+totalInvoicePrice);
        System.out.println("GT2"+totalReturnNotePrice);
        System.out.println("GT3"+ (totalInvoicePrice - totalReturnNotePrice));
        return totalInvoicePrice - totalReturnNotePrice;

    }

    @Override
    public long calculateRevenueForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        int month = currentMonth.getMonthValue();
        int year = currentMonth.getYear();

        long totalInvoicePrice = invoiceService.getTotalPriceForMonth(month,year);
        long totalReturnNotePrice = getTotalPriceForMonth(month,year);
        System.out.println("GT1"+totalInvoicePrice);
        System.out.println("GT2"+totalReturnNotePrice);
        System.out.println("GT3"+ (totalInvoicePrice - totalReturnNotePrice));
        return totalInvoicePrice - totalReturnNotePrice;
    }
    public long calculateRevenueForCurrentMonth(int month,  int year ) {
        long totalInvoicePrice = invoiceService.getTotalPriceForMonth(month,year);
        long totalReturnNotePrice = getTotalPriceForMonth(month,year);
        System.out.println("GT1"+totalInvoicePrice);
        System.out.println("GT2"+totalReturnNotePrice);
        System.out.println("GT3"+ (totalInvoicePrice - totalReturnNotePrice));
        return totalInvoicePrice - totalReturnNotePrice;
    }
    @Override
    public List <MonthRevenue> getRevenueNMonth()
    {
        List <MonthRevenue> revenues = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();
        int month = currentMonth.getMonthValue();
        int year = currentMonth.getYear();

        for ( int i=1 ; i<= month ;i++)
        {
            revenues.add( new MonthRevenue("Tháng "+i,calculateRevenueForCurrentMonth(i,year) ));
        }
        return revenues;
    }


}
