package com.example.mlservice.client;
import com.example.mlservice.dto.response.BatchDetailInfo;
import com.example.mlservice.dto.response.Customer;
import com.example.mlservice.dto.response.Invoice;
import com.example.mlservice.dto.response.InvoiceDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:8087")
public interface OrderClient {
    //ok
    @GetMapping( value = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Customer>> getAllCustomers();
    // lấy ds trả hàng của khách hàng tt:3 ok
    @GetMapping(value = "/api/invoices/status", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Invoice>> getInvoicesReturnOrder(@RequestParam Integer status, @RequestParam Long warehouseId);
    // lấy chi tiết hóa đơn
    @GetMapping(value = "/api/invoice-details/invoice/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<InvoiceDetailResponse> getInvoiceDetailsByInvoiceId(@PathVariable Long invoiceId);
    // lấy danh sach hóa đơn
    @GetMapping(value = "/api/invoices", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Invoice>> getAllInvoices(@RequestParam Long warehouseId);
    // lấy chi tiết trả hàng của khách hàng
    @GetMapping(value = "/api/return-details/return-order/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<InvoiceDetailResponse> getReturnOrders(@PathVariable Long invoiceId);

}
