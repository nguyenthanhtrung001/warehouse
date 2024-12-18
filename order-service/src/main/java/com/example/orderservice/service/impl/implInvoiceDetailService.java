package com.example.orderservice.service.impl;

import com.example.orderservice.client.MlClient;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.response.InvoiceDetailResponse;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.dto.response.SalesData;
import com.example.orderservice.entity.InvoiceDetail;
import com.example.orderservice.repository.InvoiceDetailRepository;

import com.example.orderservice.repository.ReturnDetailRepository;
import com.example.orderservice.service.IInvoiceDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class implInvoiceDetailService implements IInvoiceDetailService {

    @Autowired
    private  InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private ReturnDetailRepository returnDetailRepository;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private MlClient mlClient;

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
    public List<ProductQuantity> getProductQuantities(Integer top) {
        Pageable pageable = PageRequest.of(0, top);
        return invoiceDetailRepository.findProductQuantities(pageable);
    }
    @Override
    public List<ProductQuantity> getProductQuantities(Long warehouseId, Integer top) {
        Pageable pageable = PageRequest.of(0, top);
        return invoiceDetailRepository.findProductQuantitiesByWarehouseId(warehouseId, pageable);
    }


    @Override
    public List<ProductQuantity> getProductQuantitiesForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        List<ProductQuantity> ProductQuantity= invoiceDetailRepository.findProductQuantitiesForMonth(startOfMonth, endOfMonth);
        for (ProductQuantity productQuantity :ProductQuantity) {
            try{
                String name = productClient.getNameProductByID(productQuantity.getProductId());
                productQuantity.setProductName(name);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        return ProductQuantity;
    }

    // hệ thống
    @Override
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        List<ProductQuantity> ProductQuantity= invoiceDetailRepository.findProductQuantitiesForMonth(startOfMonth, endOfMonth);

        return ProductQuantity;
    }
    @Override
    public List<ProductQuantity> getProductQuantitiesForMonthYear(int month, int year, Long warehouseId) {
        YearMonth specifiedMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = specifiedMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = specifiedMonth.atEndOfMonth().atTime(23, 59, 59);

        return invoiceDetailRepository.findProductQuantitiesForMonth(startOfMonth, endOfMonth, warehouseId);
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

    @Override
    public Integer getTotalSoldProductForLastWeek(Long productId) {
        // Lấy ngày đầu và cuối của tuần trước đó
        LocalDateTime startOfLastWeek = LocalDate.now().minus(1, ChronoUnit.WEEKS).with(java.time.DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfLastWeek = startOfLastWeek.plusDays(6).with(LocalTime.MAX);

        // Tổng số lượng bán ra từ InvoiceDetail trong tuần trước
        Integer totalSold = invoiceDetailRepository.sumQuantityByProductIdAndDateRange(productId, startOfLastWeek, endOfLastWeek);
        if (totalSold == null) {
            totalSold = 0;
        }
        System.out.println("Mua:"+totalSold);
        LocalDate startOfLastWeekDate = startOfLastWeek.toLocalDate();
        LocalDate endOfLastWeekDate = endOfLastWeek.toLocalDate();
        // Tổng số lượng trả lại từ ReturnDetail trong tuần trước
        Integer totalReturned = returnDetailRepository.sumQuantityByProductIdAndDateRange(productId, startOfLastWeekDate, endOfLastWeekDate);
        if (totalReturned == null) {
            totalReturned = 0;
        }
        System.out.println("Tra:"+totalReturned);
        // Tổng số lượng bán sau khi trừ số lượng trả lại
        return totalSold - totalReturned;
    }

    @Override
    public String exportSalesToCsv(String filePath) {
        // Lấy ngày bắt đầu từ file CSV
        LocalDateTime startDate = getMaxDateFromCsv(filePath);

        // Nếu không có ngày bắt đầu, sử dụng một ngày mặc định (ví dụ: 30 ngày trước)
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }

        System.out.println("Ngày bắt đầu: " + startDate.toString());

        // Lấy dữ liệu từ repository
        List<SalesData> salesDataList = invoiceDetailRepository.findTotalSalesByDateAndProduct(startDate);

        if (salesDataList.isEmpty()) {
            return "No sales data found after " + startDate.toString();
        }

        try {
            // 1. Xuất dữ liệu ra file CSV
            writeSalesToCsv(salesDataList, filePath);
            // 2. Gọi Feign Client để upload file lên ml-service
            ResponseEntity<String> response = mlClient.uploadFile(filePath);
            // Kiểm tra phản hồi từ Feign Client
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Sales data exported to " + filePath + " and uploaded to BigQuery successfully!";
            } else {
                return "Error uploading file to BigQuery: " + response.getBody();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error exporting sales data: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private LocalDateTime getMaxDateFromCsv(String filePath) {
        LocalDateTime maxDate = null;

        // Định dạng thời gian đầy đủ ISO 8601
        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Bỏ qua dòng header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0) {
                    String timestampStr = values[0];

                    // Thêm giây nếu thiếu
                    if (timestampStr.length() == 16) { // yyyy-MM-dd'T'HH:mm
                        timestampStr += ":00";
                    }

                    try {
                        LocalDateTime currentDate = LocalDateTime.parse(timestampStr, isoDateTimeFormatter);

                        // Cập nhật ngày lớn nhất
                        if (maxDate == null || currentDate.isAfter(maxDate)) {
                            maxDate = currentDate;
                        }
                    } catch (DateTimeParseException e) {
                        // Ghi log nếu có lỗi và tiếp tục
                        System.err.println("Lỗi định dạng thời gian: " + timestampStr);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxDate;
    }

    private void writeSalesToCsv(List<SalesData> salesDataList, String filePath) throws IOException {
        // Định dạng thời gian ISO 8601
        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        File file = new File(filePath);
        boolean isNewFile = !file.exists() || file.length() == 0; // Kiểm tra xem file có tồn tại hoặc rỗng không

        try (FileWriter writer = new FileWriter(filePath, true)) { // Mở file ở chế độ append
            // Viết header nếu file rỗng
            if (isNewFile) {
                writer.append("timestamp,item_id,demand\n");
            }

            for (SalesData salesData : salesDataList) {
                LocalDateTime printDate = salesData.getPrintDate();

                // Chuyển đổi printDate sang định dạng đầy đủ ISO 8601
                String formattedTimestamp = printDate.format(isoDateTimeFormatter);

                // Ghi dữ liệu cần thiết vào file CSV
                writer.append(formattedTimestamp)
                        .append(",")
                        .append(String.valueOf(salesData.getProductId()))
                        .append(",")
                        .append(String.valueOf(salesData.getTotalDemand()))
                        .append("\n");
            }
        }
    }
}
