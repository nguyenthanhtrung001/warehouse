package com.example.mlservice.controller;

import com.example.mlservice.dto.request.Import_Export_Request;
import com.example.mlservice.dto.request.InventoryCheckSlipRequest;
import com.example.mlservice.dto.response.*;
import com.example.mlservice.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    IChatService iChatService;

    // lấy danh sách sản phẩm
    @GetMapping("/products/has-batch-location-warehouse/{warehouse}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(@PathVariable Long warehouse) {
        try {
            List<ProductResponse> products = iChatService.getProductsWithLocationBatch(warehouse);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách sản phẩm thành công", products)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage(), null)
            );
        }
    }
    // lấy danh sách khách hàng
    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomers() {
        try {
            List<Customer> customers = iChatService.getAllCustomers();
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách khách hàng thành công", customers)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách khách hàng: " + e.getMessage(), null)
            );
        }
    }
    // Lấy danh sách hóa đơn, trả hàng của khách hàng (theo trạng thái và kho)
    @GetMapping(value = "/invoices/status", produces = "application/json")
    public ResponseEntity<ApiResponse<List<Invoice>>> getInvoicesByStatusAndWarehouse(
            @RequestParam Integer status,
            @RequestParam Long warehouseId) {
        try {
            List<Invoice> invoices = iChatService.getInvoicesByStatusAndWarehouse(status, warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách hóa đơn thành công", invoices)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách hóa đơn: " + e.getMessage(), null)
            );
        }
    }

    @GetMapping(value = "/invoice-details/invoice/{invoiceId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<InvoiceDetailResponse>>> getInvoiceDetailsById(@PathVariable Long invoiceId) {
        try {
            List<InvoiceDetailResponse> invoiceDetails = iChatService.getInvoiceDetailsById(invoiceId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy chi tiết hóa đơn thành công", invoiceDetails)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage(), null)
            );
        }
    }

    // Lấy danh sách hóa đơn theo ID kho
    @GetMapping(value = "/invoices", produces = "application/json")
    public ResponseEntity<ApiResponse<List<Invoice>>> getAllInvoices(@RequestParam Long warehouseId) {
        try {
            List<Invoice> invoices = iChatService.getAllInvoices(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách hóa đơn thành công", invoices)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách hóa đơn: " + e.getMessage(), null)
            );
        }
    }

    // Lấy danh sách chi tiết đơn trả hàng theo invoiceId
    @GetMapping(value = "/return-details/return-order/{invoiceId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<InvoiceDetailResponse>>> getReturnOrders(@PathVariable Long invoiceId) {
        try {
            List<InvoiceDetailResponse> returnOrders = iChatService.getReturnOrders(invoiceId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách chi tiết đơn trả hàng thành công", returnOrders)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách chi tiết đơn trả hàng: " + e.getMessage(), null)
            );
        }
    }
    // Lấy chi tiết trong lô hàng theo productId và warehouseId
    @GetMapping(value = "/batch-details/product/{productId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<BatchDetail>>> getBatchDetailsByProductId(
            @PathVariable Long productId,
            @RequestParam Long warehouseId) {
        try {
            List<BatchDetail> batchDetails = iChatService.getBatchDetailsByProductId(productId, warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy chi tiết trong lô hàng thành công", batchDetails)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy chi tiết trong lô hàng: " + e.getMessage(), null)
            );
        }
    }
    // Lấy danh sách vị trí trong kho hàng
    @GetMapping(value = "/locations/warehouse/{warehouseId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<Location>>> getAllLocationsInWarehouse(@PathVariable Long warehouseId) {
        try {
            List<Location> locations = iChatService.getAllLocationsInWarehouse(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách vị trí trong kho thành công", locations)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách vị trí trong kho: " + e.getMessage(), null)
            );
        }
    }

    // Tạo phiếu kiểm kho
    @PostMapping(value = "/inventory-check-slips", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<InventoryCheckSlip>> createInventoryCheckSlip(
            @RequestBody InventoryCheckSlipRequest inventoryCheckSlipRequest) {
        try {
            InventoryCheckSlip inventoryCheckSlip = iChatService.createInventoryCheckSlip(inventoryCheckSlipRequest);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Tạo phiếu kiểm kho thành công", inventoryCheckSlip)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi tạo phiếu kiểm kho: " + e.getMessage(), null)
            );
        }
    }
    // Lấy danh sách lô hàng theo warehouseId
    @GetMapping(value = "/batches/batches/details", produces = "application/json")
    public ResponseEntity<ApiResponse<List<BatchDetailInfo>>> getBatchDetailsByWarehouseId(@RequestParam Long warehouseId) {
        try {
            List<BatchDetailInfo> batchDetails = iChatService.getBatchDetailsByWarehouseId(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách lô hàng thành công", batchDetails)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách lô hàng: " + e.getMessage(), null)
            );
        }
    }
    // Lấy danh sách tất cả nhà cung cấp
    @GetMapping(value = "/suppliers", produces = "application/json")
    public ResponseEntity<ApiResponse<List<Supplier>>> getAllSuppliers() {
        try {
            List<Supplier> suppliers = iChatService.getAllSuppliers();
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách nhà cung cấp thành công", suppliers)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách nhà cung cấp: " + e.getMessage(), null)
            );
        }
    }

    // Lấy danh sách tất cả kho hàng
    @GetMapping(value = "/warehouses", produces = "application/json")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getAllWarehouses() {
        try {
            List<Warehouse> warehouses = iChatService.getAllWarehouses();
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách kho hàng thành công", warehouses)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách kho hàng: " + e.getMessage(), null)
            );
        }
    }
    // Lấy danh sách nhập hàng theo warehouseId
    @GetMapping(value = "/receipts", produces = "application/json")
    public ResponseEntity<ApiResponse<List<Receipt>>> getAllReceipts(@RequestParam("warehouseId") Long warehouseId) {
        try {
            List<Receipt> receipts = iChatService.getAllReceipts(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách phiếu nhập thành công", receipts)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách phiếu nhập: " + e.getMessage(), null)
            );
        }
    }

    // Lấy danh sách hủy hàng khỏi kho theo warehouseId
    @GetMapping(value = "/deliveryNotes/cancel", produces = "application/json")
    public ResponseEntity<ApiResponse<List<DeliveryNote>>> getAllDeliveryNotesCancel(@RequestParam Long warehouseId) {
        try {
            List<DeliveryNote> deliveryNotes = iChatService.getAllDeliveryNotesCancel(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách hủy hàng khỏi kho thành công", deliveryNotes)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách hủy hàng khỏi kho: " + e.getMessage(), null)
            );
        }
    }
    // Lấy danh sách chuyển kho theo warehouseId
    @GetMapping(value = "/deliveryNotes/transfer", produces = "application/json")
    public ResponseEntity<ApiResponse<List<DeliveryNote>>> getAllTransfer(@RequestParam Long warehouseId) {
        try {
            List<DeliveryNote> transferNotes = iChatService.getAllTransfer(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách chuyển kho thành công", transferNotes)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách chuyển kho: " + e.getMessage(), null)
            );
        }
    }
    // Lấy ds yêu cầu nhập kho theo warehouseId
    @GetMapping(value = "/deliveryNotes/import-transfer", produces = "application/json")
    public ResponseEntity<ApiResponse<List<DeliveryNote>>> getAllImportTransfer(@RequestParam Long warehouseId) {
        try {
            List<DeliveryNote> importTransfers = iChatService.getAllImportTransfer(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách yêu cầu nhập kho thành công", importTransfers)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách yêu cầu nhập kho: " + e.getMessage(), null)
            );
        }
    }
    // Lấy chi tiết các phiếu xuất - hủy - chuyển - trả NCC theo noteId
    @GetMapping(value = "/deliveryNotes/{noteId}/details", produces = "application/json")
    public ResponseEntity<ApiResponse<List<NoteDetailResponse>>> getNoteDetails(@PathVariable Long noteId) {
        try {
            List<NoteDetailResponse> noteDetails = iChatService.getNoteDetails(noteId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy chi tiết phiếu thành công", noteDetails)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy chi tiết phiếu: " + e.getMessage(), null)
            );
        }
    }
    // Lấy danh sách trả nhà cung cấp theo warehouseId
    @GetMapping(value = "/deliveryNotes", produces = "application/json")
    public ResponseEntity<ApiResponse<List<DeliveryNote>>> getAllDeliveryNotes(@RequestParam Long warehouseId) {
        try {
            List<DeliveryNote> deliveryNotes = iChatService.getAllDeliveryNotes(warehouseId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy danh sách trả nhà cung cấp thành công", deliveryNotes)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy danh sách trả nhà cung cấp: " + e.getMessage(), null)
            );
        }
    }
    // Lấy chi tiết nhập hàng theo receiptId
    @GetMapping(value = "/receipt-details/{receiptId}/details", produces = "application/json")
    public ResponseEntity<ApiResponse<List<ReceiptDetailResponse>>> getReceiptDetails(@PathVariable Long receiptId) {
        try {
            List<ReceiptDetailResponse> receiptDetails = iChatService.getReceiptDetails(receiptId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy chi tiết nhập hàng thành công", receiptDetails)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy chi tiết nhập hàng: " + e.getMessage(), null)
            );
        }
    }
    // Tạo phiếu nhập với chi tiết
    @PostMapping(value = "/receipts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<Object>> createReceiptWithDetails(@RequestBody Import_Export_Request importExportRequest) {
        try {
            Object receipt = iChatService.createReceiptWithDetails(importExportRequest);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Tạo phiếu nhập thành công", receipt)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi tạo phiếu nhập: " + e.getMessage(), null)
            );
        }
    }
    // Lấy thông tin nhân viên theo ID
    @GetMapping(value = "/employees/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable Long id) {
        try {
            Employee employee = iChatService.getEmployeeById(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy thông tin nhân viên thành công", employee)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy thông tin nhân viên: " + e.getMessage(), null)
            );
        }
    }
    @GetMapping(value = "/batch-details/batch/{batchId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<BatchDetailDTO>>> getBatchDetailsByBatchId(@PathVariable Long batchId) {
        try {
            List<BatchDetailDTO> batchDetails = iChatService.getBatchDetailsByBatchId(batchId);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy thông tin chi tiết lô hàng thành công", batchDetails)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy thông tin chi tiết lô hàng: " + e.getMessage(), null)
            );
        }
    }
    @GetMapping(value = "/batch-details/location/{locationId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<ProductLocation>>> getBatchDetailsByLocationId(@PathVariable Long locationId) {
        try {
            List<ProductLocation> productLocations = iChatService.getBatchDetailsByLocationId(locationId);

            if (productLocations == null || productLocations.isEmpty()) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>(false, "Không tìm thấy thông tin cho locationId: " + locationId, null)
                );
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lấy thông tin chi tiết sản phẩm thành công", productLocations)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>(false, "Lỗi khi lấy thông tin chi tiết sản phẩm: " + e.getMessage(), null)
            );
        }
    }

}
