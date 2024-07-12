package com.example.goodsservice.service.impl;

import com.example.goodsservice.client.InventoryClient;
import com.example.goodsservice.dto.*;
import com.example.goodsservice.entity.DeliveryNote;
import com.example.goodsservice.entity.Receipt;
import com.example.goodsservice.entity.ReceiptDetail;
import com.example.goodsservice.entity.Supplier;
import com.example.goodsservice.mapper.BathDetailMapper;
import com.example.goodsservice.mapper.BathMapper;
import com.example.goodsservice.repository.ReceiptDetailRepository;
import com.example.goodsservice.repository.ReceiptRepository;
import com.example.goodsservice.service.IReceiptService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class implReceiptService implements IReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;
    @Autowired
    InventoryClient inventoryClient;
    @Autowired
    BathMapper bathMapper;
    @Autowired
    BathDetailMapper bathDetailMapper;
    @Override
    public Receipt createReceipt(Receipt receipt) {
        // Gọi api tạo lô hàng trước
        return receiptRepository.save(receipt);
    }

    @Override
    public Receipt getReceiptById(Long id) {
        Optional<Receipt> receipt = receiptRepository.findById(id);
        return receipt.orElse(null);
    }

    @Override
    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }

    @Override
    public boolean updateReceipt(Long id, Receipt receipt) {
        Optional<Receipt> existingReceipt = receiptRepository.findById(id);
        if (existingReceipt.isPresent()) {
            Receipt updatedReceipt = existingReceipt.get();
            updatedReceipt.setReceiptDate(receipt.getReceiptDate());
            updatedReceipt.setSupplier(receipt.getSupplier());
            updatedReceipt.setStatus(receipt.getStatus());
            updatedReceipt.setPurchasePrice(receipt.getPurchasePrice());
            receiptRepository.save(updatedReceipt);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteReceipt(Long id) {
        if (receiptRepository.existsById(id)) {
            receiptRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateReceiptStatus(Long id, Integer status) {
        Optional<Receipt> existingReceipt = receiptRepository.findById(id);
        if (existingReceipt.isPresent()) {
            Receipt receipt = existingReceipt.get();
            receipt.setStatus(status);
            receiptRepository.save(receipt);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<DeliveryNote> getDeliveryNotesByReceiptId(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId).orElse(null);
        if (receipt == null) {
            // Xử lý trường hợp không tìm thấy Receipt với id tương ứng
            return Collections.emptyList(); // hoặc có thể ném một ngoại lệ phù hợp
        }
        return receipt.getDeliveryNotes();
    }

    @Transactional
    public Receipt createReceiptWithDetails(Import_Export_Request importExportRequest) {

        // gọi API tạo lô hàng và cập nhật số lượng cho lô hàng
        BathRequest bathRequest = bathMapper.toBathRequest(importExportRequest);
        bathRequest.setStatus(1);
        BathRequest bath = inventoryClient.createBath(bathRequest);
         /*   System.out.println("ID Name:"+bath.getBatchName());
        System.out.println("ID bath:"+bath.getId());*/

        Receipt savedReceipt = null;
        Receipt receipt = new Receipt();
       try {
           receipt.setReceiptDate(LocalDateTime.now());

           Supplier supplier =new Supplier(importExportRequest.getSupplier());
           receipt.setSupplier(supplier);
           receipt.setStatus(1);
           receipt.setPurchasePrice(importExportRequest.getPrice());
           // API lấy thông tin đăng nhập để set ID Nhân viên
            receipt.setEmployeeId(importExportRequest.getEmployeeId());
           savedReceipt = receiptRepository.save(receipt);

       }catch (Exception e)
       {
           e.printStackTrace();
       }


        for (Import_Export_DetailRequest detailRequest : importExportRequest.getImport_Export_Details()) {

        try{
            System.out.println("ID Name abc:"+detailRequest.getProduct_Id());
            Bath bathTmp =new Bath();
            bathTmp.setId(bath.getId());
            Location location = new Location();
            location.setId(importExportRequest.getLocation());
            BathDetailRequest bathDetailRequest = bathDetailMapper.toBathDetailRequest(detailRequest);
            bathDetailRequest.setBatch(bathTmp);
            bathDetailRequest.setLocation(location);
            BathDetailRequest bathDetail = inventoryClient.createDetailBath(bathDetailRequest);
            System.out.println(importExportRequest.getLocation());
            System.out.println("ID bath:"+bathDetail.getId());
        }catch (Exception e){
            e.printStackTrace();
        }


            ReceiptDetail detail = new ReceiptDetail();
            detail.setReceipt(savedReceipt);

            detail.setPurchasePrice(detailRequest.getPurchasePrice());
            detail.setQuantity(detailRequest.getQuantity());

            receiptDetailRepository.save(detail);
        }

        return savedReceipt;
    }
}
