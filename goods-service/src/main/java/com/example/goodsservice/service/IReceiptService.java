package com.example.goodsservice.service;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;

import java.util.List;

public interface  IReceiptService {

    Receipt createReceipt(Receipt receipt);
    Receipt getReceiptById(Long id);
    List<Receipt> getAllReceipts();
    boolean updateReceipt(Long id, Receipt receipt);
    boolean deleteReceipt(Long id);
    boolean updateReceiptStatus(Long id, Integer status);
    public List<DeliveryNote> getDeliveryNotesByReceiptId(Long receiptId);

    public Receipt createReceiptWithDetails(Import_Export_Request importExportRequest);


}
