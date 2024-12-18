package com.example.goodsservice.service;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.dto.response.ProductSummary;
import com.example.goodsservice.dto.response.ReceiptSummary;
import com.example.goodsservice.dto.response.ReportImportExport;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;

import java.util.List;

public interface  IReceiptService {

    ReceiptSummary getReceiptSummaryForCurrentMonth(Long warehouseId);
    public ReceiptSummary getReceiptSummaryForCurrentMonth();
    Receipt getReceiptById(Long id);
    List<Receipt> getAllReceipts(Long warehouseId);
    public List<Receipt> getAllReceiptsForReturn(Long warehouseId);
    boolean updateReceipt(Long id, Receipt receipt);
    boolean deleteReceiptUpdateStatus(Long id);
    boolean updateReceiptStatus(Long id, Integer status);
    public List<DeliveryNote> getDeliveryNotesByReceiptId(Long receiptId);

    public Receipt createReceiptWithDetails(Import_Export_Request importExportRequest);
    public Receipt createImportTransfer(Import_Export_Request importExportRequest);

    public List<ReportImportExport> createReportImportExport( Integer month, Integer year);

    public List<ProductSummary> getProductSummaryBySupplierId(Long supplierId, Long warehouseId, int year, int month);
        public List<ReportImportExport> createReportImportExport(Integer month, Integer year, Long wareHouseId);

    }
