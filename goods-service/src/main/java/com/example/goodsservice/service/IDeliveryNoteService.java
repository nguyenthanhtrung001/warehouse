package com.example.goodsservice.service;

import com.example.goodsservice.dto.Import_Export_Request;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Supplier;

import java.util.List;

public interface IDeliveryNoteService {


        DeliveryNote getDeliveryNoteById(Long id);
        public List<DeliveryNote> getAllDeliveryNotesCancel(Long warehouseId);
        public List<DeliveryNote> getAllDeliveryNotes(Long warehouseId);
        boolean updateDeliveryNote(Long id, DeliveryNote deliveryNoteDetails);
        boolean deleteDeliveryNote(Long id);
        boolean updateDeliveryNoteStatus(Long id, Integer status);
        public DeliveryNote createDeliveryNoteWithDetails(Import_Export_Request importExportRequest);
        public DeliveryNote createDeliveryNote_Delete_WithDetails(Import_Export_Request importExportRequest);

}
