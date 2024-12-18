package com.example.mlservice.service.impl;

import com.example.mlservice.client.*;
import com.example.mlservice.dto.request.Import_Export_Request;
import com.example.mlservice.dto.request.InventoryCheckSlipRequest;
import com.example.mlservice.dto.response.*;
import com.example.mlservice.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class implChat implements IChatService {

    @Autowired
    private  ProductClient productClient;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private InventoryClient inventoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private EmployeeClient employeeClient;

    @Override
    public List<ProductResponse> getProductsWithLocationBatch(Long warehouseId) {
        return productClient.getAllProductsHasLocationBatch(warehouseId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return orderClient.getAllCustomers().getBody();
    }

    @Override
    public List<Invoice> getAllInvoices(Long warehouseId) {
        return orderClient.getAllInvoices(warehouseId).getBody();
    }

    // danh sách hóa đơn theo trạng thái 1-đơn đặt, 2 đã thanh toán, 3 tồn tại trả
    @Override
    public List<Invoice> getInvoicesByStatusAndWarehouse(Integer status, Long warehouseId) {
        return orderClient.getInvoicesReturnOrder(status, warehouseId).getBody();
    }

    @Override
    public List<InvoiceDetailResponse> getInvoiceDetailsById(Long invoiceId) {
        return orderClient.getInvoiceDetailsByInvoiceId(invoiceId);
    }

    @Override
    public List<InvoiceDetailResponse> getReturnOrders(Long invoiceId) {
        return orderClient.getReturnOrders(invoiceId);
    }

    @Override
    public List<BatchDetail> getBatchDetailsByProductId(Long productId, Long warehouseId) {
        return inventoryClient.getBatchDetailsByProductId(productId, warehouseId);
    }

    @Override
    public List<Location> getAllLocationsInWarehouse(Long warehouseId) {
        return inventoryClient.getAllLocationsInWarehouse(warehouseId).getBody();
    }

    @Override
    public InventoryCheckSlip createInventoryCheckSlip(InventoryCheckSlipRequest inventoryCheckSlipRequest) {
        return inventoryClient.createInventoryCheckSlip(inventoryCheckSlipRequest).getBody();

    }

    @Override
    public List<BatchDetailInfo> getBatchDetailsByWarehouseId(Long warehouseId) {
        return inventoryClient.getBatchDetailsByWarehouseId(warehouseId);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return goodsClient.getAllSuppliers().getBody();
    }
    @Override
    public List<Warehouse> getAllWarehouses() {
        return goodsClient.getAllWarehouses();
    }
    @Override
    public List<Receipt> getAllReceipts(Long warehouseId) {
        return goodsClient.getAllReceipts(warehouseId).getBody();
    }
    @Override
    public List<DeliveryNote> getAllDeliveryNotesCancel(Long warehouseId) {
        return goodsClient.getAllDeliveryNotesCancel(warehouseId).getBody();
    }
    @Override
    public List<DeliveryNote> getAllTransfer(Long warehouseId) {
        return goodsClient.getAllTransfer(warehouseId).getBody();
    }
    @Override
    public List<DeliveryNote> getAllImportTransfer(Long warehouseId) {
        return goodsClient.getAllImportTransfer(warehouseId).getBody();
    }
    @Override
    public List<NoteDetailResponse> getNoteDetails(Long noteId) {
        return goodsClient.getNoteDetails(noteId).getBody();
    }
    @Override
    public List<DeliveryNote> getAllDeliveryNotes(Long warehouseId) {
        return goodsClient.getAllDeliveryNotes(warehouseId).getBody();
    }
    @Override
    public List<ReceiptDetailResponse> getReceiptDetails(Long receiptId) {
        return goodsClient.getReceiptDetails(receiptId).getBody();
    }
    @Override
    public Object createReceiptWithDetails(Import_Export_Request importExportRequest) {
        return goodsClient.createReceiptWithDetails(importExportRequest).getBody();
    }
    @Override
    public Employee getEmployeeById(Long id) {
        return employeeClient.getEmployeeById(id).getBody();
    }

    @Override
    public List<BatchDetailDTO> getBatchDetailsByBatchId(Long batchId) {
        return inventoryClient.getBatchDetailsByBatchId(batchId);
    }

    @Override
    public List<ProductLocation> getBatchDetailsByLocationId(Long locationId) {
        return inventoryClient.getBatchDetailsByLocationId(locationId).getBody();
    }
}
