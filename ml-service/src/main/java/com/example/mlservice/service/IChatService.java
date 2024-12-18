package com.example.mlservice.service;

import com.example.mlservice.dto.request.Import_Export_Request;
import com.example.mlservice.dto.request.InventoryCheckSlipRequest;
import com.example.mlservice.dto.response.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IChatService {

    // product-client
    public List<ProductResponse> getProductsWithLocationBatch(Long warehouseId);
    // order-client
    public List<Customer> getAllCustomers();
    List<Invoice> getAllInvoices(Long warehouseId);
    List<Invoice> getInvoicesByStatusAndWarehouse(Integer status, Long warehouseId);
    List<InvoiceDetailResponse> getInvoiceDetailsById(Long invoiceId);
    List<InvoiceDetailResponse> getReturnOrders(Long invoiceId);

    // inventory-client
    List<BatchDetail> getBatchDetailsByProductId(Long productId, Long warehouseId);
    List<Location> getAllLocationsInWarehouse(Long warehouseId);
    InventoryCheckSlip createInventoryCheckSlip(InventoryCheckSlipRequest inventoryCheckSlipRequest);
    List<BatchDetailInfo> getBatchDetailsByWarehouseId(Long warehouseId);

    // goods-client
    List<Supplier> getAllSuppliers();
    List<Warehouse> getAllWarehouses();
    List<Receipt> getAllReceipts(Long warehouseId);
    List<DeliveryNote> getAllDeliveryNotesCancel(Long warehouseId);
    List<DeliveryNote> getAllTransfer(Long warehouseId);
    List<DeliveryNote> getAllImportTransfer(Long warehouseId);
    List<NoteDetailResponse> getNoteDetails(Long noteId);
    List<DeliveryNote> getAllDeliveryNotes(Long warehouseId);
    List<ReceiptDetailResponse> getReceiptDetails(Long receiptId);
    Object createReceiptWithDetails(Import_Export_Request importExportRequest);
    Employee getEmployeeById(Long id);
    List<BatchDetailDTO> getBatchDetailsByBatchId (Long batchId);
    List<ProductLocation> getBatchDetailsByLocationId(Long locationId);


}
