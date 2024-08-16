package com.example.orderservice.service.impl;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.InvoiceDetailRequest;
import com.example.orderservice.dto.InvoiceRequest;

import com.example.orderservice.dto.response.OrderQuantity;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Invoice;
import com.example.orderservice.entity.InvoiceDetail;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.InvoiceDetailRepository;
import com.example.orderservice.repository.InvoiceRepository;
import com.example.orderservice.security.EncoderDecoder;
import com.example.orderservice.service.IInvoiceDetailService;
import com.example.orderservice.service.IInvoiceService;
import com.example.orderservice.service.IReturnDetailService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class implInvoiceService implements IInvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private IReturnDetailService returnDetailService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    InventoryClient inventoryClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    IInvoiceDetailService invoiceDetailService;

    private ModelMapper modelMapper = new ModelMapper();


    @Transactional
    public Invoice createInvoice(InvoiceRequest orderRequest) {
        // Find customer by ID
        Customer customer = customerRepository.findById(orderRequest.getCustomer())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Create new Invoice
        // Lấy ngày giờ hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();

        Invoice invoice = new Invoice();
        invoice.setPrintDate(currentDateTime); // Set print date to current date
        invoice.setCustomer(customer);
        invoice.setStatus(1); // Assuming status 1 means Success
        invoice.setPrice(orderRequest.getPrice()); // Calculate total price
        invoice.setEmployeeId(orderRequest.getEmployeeId());
        invoice.setNote(orderRequest.getNote());

        // Save Invoice to get ID generated
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Create InvoiceDetails
        List<InvoiceDetail> orderDetails = new ArrayList<>();
        for (InvoiceDetailRequest detailRequest : orderRequest.getOrder_Details()) {
            InvoiceDetail detail = new InvoiceDetail();
            // InvoiceDetail detail = modelMapper.map(detailRequest,InvoiceDetail.class);
            detail.setInvoiceId(savedInvoice);

            detail.setProductId(detailRequest.getProduct_Id());
            detail.setQuantity(detailRequest.getQuantity());
            detail.setPurchasePrice(detailRequest.getPurchasePrice());
            List<OrderQuantity> orderQuantity = inventoryClient.updateDetailBathWithProduct(detail.getProductId(), detail.getQuantity());

            String note = EncoderDecoder.encodeToJsonBase64(orderQuantity);
            System.out.println("Idtmp:" + note);
            detail.setNote_return(note);
//            List<OrderQuantity> abc= EncoderDecoder.decodeFromJsonBase64("W3siYmF0aERldGFpbF9JZCI6MiwicXVhbnRpdHkiOjF9XQ==");

            orderDetails.add(detail);
        }


        // Save all InvoiceDetails
        invoiceDetailRepository.saveAll(orderDetails);


        return savedInvoice;
    }

    @Override
    public long getTotalPriceForCurrentWeek() {
        // Lấy ngày đầu tuần (thứ Hai) và cuối tuần (Chủ Nhật) của tuần hiện tại
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Chuyển đổi sang LocalDateTime để so sánh
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX);

        // Truy vấn cơ sở dữ liệu để lấy danh sách hóa đơn trong khoảng thời gian này
        List<Invoice> invoices = invoiceRepository.findByPrintDateBetween(startDateTime, endDateTime);

        // Tính tổng giá
        return invoices.stream()
                .filter(invoice -> invoice.getPrice() != null) // Kiểm tra null
                .mapToLong(Invoice::getPrice)
                .sum();

    }

    @Override
    public long getTotalPriceForCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(LocalTime.MAX);

        List<Invoice> invoices = invoiceRepository.findByPrintDateBetween(startDateTime, endDateTime);

        return invoices.stream()
                .filter(invoice -> invoice.getPrice() != null) // Kiểm tra null
                .mapToLong(Invoice::getPrice)
                .sum();

    }

    // Helper method to calculate total price based on details


    @Override
    public Invoice getInvoiceById(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        return invoice.orElse(null);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAllInvoicesWithStatusNotInZeroOrOne();
    }

    @Override
    public List<Invoice> getAllInvoicesWithStatus(Integer status) {
        return invoiceRepository.findByStatus(status);
    }

    @Override
    public boolean updateInvoice(Long id, Invoice invoice) {
        return false;
    }

    @Transactional
    public boolean updateInvoiceStatus(Long invoiceId, Integer newStatus) {
        return invoiceRepository.findById(invoiceId)
                .map(invoice -> {
                    invoice.setStatus(newStatus);
                    invoiceRepository.save(invoice);
                    return true;
                })
                .orElse(false);
    }


    @Override
    public Invoice updateInvoice(Long invoiceId, InvoiceRequest order) {
        // Find existing Invoice by ID
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        if (!optionalInvoice.isPresent()) {
            throw new IllegalArgumentException("Invoice not found");
        }
        Invoice existingInvoice = optionalInvoice.get();

        // Update customer (if changed)
        Customer customer = customerRepository.findById(order.getCustomer())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        existingInvoice.setCustomer(customer);

        // Update price (if needed)
        existingInvoice.setPrice(order.getPrice());

        // Update InvoiceDetails
        List<InvoiceDetail> updatedInvoiceDetails = new ArrayList<>();
        for (InvoiceDetailRequest detailRequest : order.getOrder_Details()) {
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setInvoiceId(existingInvoice);
            invoiceDetail.setProductId(detailRequest.getProduct_Id());
            invoiceDetail.setQuantity(detailRequest.getQuantity());
            updatedInvoiceDetails.add(invoiceDetail);

        }

        // Delete existing InvoiceDetails and save updated ones
//        invoiceDetailRepository.deleteByInvoiceId(invoiceId);
        invoiceDetailRepository.saveAll(updatedInvoiceDetails);

        // Save and return updated Invoice
        return invoiceRepository.save(existingInvoice);
    }

    @Transactional
    public boolean deleteInvoiceWithStatus_1(Long id) {

        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice.getStatus() != 1) return false;

        List<InvoiceDetail> receiptDetails = invoiceDetailService.getDetailsByInvoiceId(id);
        for (InvoiceDetail detail : receiptDetails) {
            List<OrderQuantity> bathQuantity = EncoderDecoder.decodeFromJsonBase64(detail.getNote_return());
            for (OrderQuantity orderQuantity : bathQuantity) {
                inventoryClient.updateQuantityForReturnOrder(orderQuantity.getBathDetail_Id(), detail.getQuantity());
            }

            Boolean tmp = invoiceDetailService.deleteInvoiceDetail(detail.getId());
            if (tmp == true) System.out.println("Xoa Thanh cong chi tiet nhap");
        }
        invoiceRepository.deleteById(id);
        return true;


    }

    @Override
    public Map<String, Object> getProductSalesSummary(int year) {
        Map<String, Object> result = new HashMap<>();

        // Tạo map lưu trữ số lượng sản phẩm theo tháng và theo productId
        Map<Long, Map<String, Long>> productSalesMap = new HashMap<>();

        for (Month month : Month.values()) {
            // Xác định ngày bắt đầu và kết thúc của tháng hiện tại
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            // Lấy danh sách số lượng sản phẩm từ hóa đơn cho tháng hiện tại
            List<ProductQuantity> listImportOrder = Optional.ofNullable(invoiceDetailService.getProductQuantitiesForMonthYear(month.getValue(), year))
                    .orElse(Collections.emptyList()); // Sử dụng Collections.emptyList() để tối ưu hóa

            // Cập nhật số lượng sản phẩm vào map theo productId
            for (ProductQuantity pq : listImportOrder) {
                Long productId = pq.getProductId();
                Long quantity = pq.getQuantity();

                productSalesMap.putIfAbsent(productId, new HashMap<>());
                Map<String, Long> monthlySales = productSalesMap.get(productId);

                monthlySales.put("month_" + month.getValue(),
                        monthlySales.getOrDefault("month_" + month.getValue(), 0L) + quantity);
            }
        }

        // Chuyển đổi dữ liệu thành định dạng biểu đồ
        List<Map<String, Object>> chartDataList = new ArrayList<>();

        for (Map.Entry<Long, Map<String, Long>> entry : productSalesMap.entrySet()) {
            Long productId = entry.getKey();
            Map<String, Long> monthlySales = entry.getValue();

            Map<String, Object> chartFormat = new HashMap<>();
            String name="";
           try {
                name = productClient.getNameProductByID(productId);
           }catch (Exception e){
               e.printStackTrace();
           }
            chartFormat.put("name",name);

            // Lấy danh sách số lượng tổng cho từng tháng
            List<Long> dataValues = Arrays.stream(Month.values())
                    .map(month -> monthlySales.getOrDefault("month_" + (month.getValue()), 0L))
                    .collect(Collectors.toList());

            // Thêm dữ liệu vào map biểu đồ
            chartFormat.put("data", dataValues);

            // Thêm dữ liệu biểu đồ vào danh sách
            chartDataList.add(chartFormat);
        }

        // Thêm dữ liệu biểu đồ vào kết quả trả về
        result.put("chartData", chartDataList);

        return result;
    }
}
