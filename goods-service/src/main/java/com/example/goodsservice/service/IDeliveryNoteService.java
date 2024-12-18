package com.example.goodsservice.service;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.dto.response.DeliverySummaryResponse;
import com.example.goodsservice.entity.DeliveryNote;

import java.util.List;

public interface IDeliveryNoteService {


        DeliveryNote getDeliveryNoteById(Long id);
        public List<DeliveryNote> getAllDeliveryNotesCancel(Long warehouseId);
        public List<DeliveryNote> getAllTransfer(Long warehouseId);
        public List<DeliveryNote> getAllImportTransfer(Long warehouseId);
        public List<DeliveryNote> getAllDeliveryNotes(Long warehouseId);
        boolean updateDeliveryNote(Long id, DeliveryNote deliveryNoteDetails);
        boolean deleteDeliveryNote(Long id);
        public boolean cancelTransfer(Long id, String reason);
        boolean updateDeliveryNoteStatus(Long id, Integer status);
        public DeliveryNote createDeliveryNoteWithDetails(Import_Export_Request importExportRequest);
        public DeliveryNote createDeliveryNote_Delete_WithDetails(Import_Export_Request importExportRequest);
        public DeliveryNote createTransfer(Import_Export_Request importExportRequest);
        public DeliverySummaryResponse getSummaryByTypeAndStatus(Integer type, Integer status);
}
