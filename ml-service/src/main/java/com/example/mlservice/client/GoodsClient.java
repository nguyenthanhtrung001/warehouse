package com.example.mlservice.client;

import com.example.mlservice.dto.request.Import_Export_Request;
import com.example.mlservice.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "goods-service", url = "http://localhost:8088")
public interface GoodsClient {
    @GetMapping(value = "/api/suppliers", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Supplier>> getAllSuppliers();

    @GetMapping(value = "/api/warehouses", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Warehouse> getAllWarehouses();

    // Tạo phiếu nhập
    @PostMapping(value = "/api/receipts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createReceiptWithDetails(@RequestBody Import_Export_Request importExportRequest);
    // ds trả nhà cung cấp
    @GetMapping(value = "/api/deliveryNotes", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DeliveryNote>> getAllDeliveryNotes(@RequestParam Long warehouseId);
    // lấy chi tiết các phiếu xuất - hủy - chuyển - trả ncc
    @GetMapping(value = "/api/deliveryNotes/{noteId}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<NoteDetailResponse>> getNoteDetails(@PathVariable Long noteId);
    // lấy chi tiết yêu cầu nhập kho
    @GetMapping(value = "/api/deliveryNotes/import-transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DeliveryNote>> getAllImportTransfer(@RequestParam Long warehouseId);
    // lấy danh sách chuyển kho
    @GetMapping(value = "/api/deliveryNotes/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DeliveryNote>> getAllTransfer(@RequestParam Long warehouseId);
    // lấy danh sách hủy hàng khỏi kho
    @GetMapping(value = "/api/deliveryNotes/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DeliveryNote>> getAllDeliveryNotesCancel (@RequestParam Long warehouseId);
    // lấy danh sách phiếu nhập
    @GetMapping(value = "/api/receipts", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Receipt>> getAllReceipts (@RequestParam("warehouseId") Long warehouseId);
    // lấy chi tiết nhập hàng
    @GetMapping(value = "/api/receipt-details/{receiptId}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ReceiptDetailResponse>> getReceiptDetails (@PathVariable Long receiptId);
}
