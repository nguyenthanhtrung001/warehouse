package com.example.goodsservice.controller;

import com.example.goodsservice.entity.ReceiptDetail;
import com.example.goodsservice.service.IReceiptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/receipt-details")
public class ReceiptDetailController {
    @Autowired
    private  IReceiptDetailService receiptDetailService;


    @PostMapping
    public ResponseEntity<ReceiptDetail> createReceiptDetail(@RequestBody ReceiptDetail receiptDetail) {
        ReceiptDetail createdReceiptDetail = receiptDetailService.createReceiptDetail(receiptDetail);
        return new ResponseEntity<>(createdReceiptDetail, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDetail> getReceiptDetailById(@PathVariable Long id) {
        Optional<ReceiptDetail> receiptDetail = receiptDetailService.getReceiptDetailById(id);
        return receiptDetail.map(detail -> new ResponseEntity<>(detail, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ReceiptDetail>> getAllReceiptDetails() {
        List<ReceiptDetail> receiptDetails = receiptDetailService.getAllReceiptDetails();
        return new ResponseEntity<>(receiptDetails, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceiptDetail> updateReceiptDetail(@PathVariable Long id, @RequestBody ReceiptDetail receiptDetail) {
        boolean updated = receiptDetailService.updateReceiptDetail(id, receiptDetail);
        if (updated) {
            return new ResponseEntity<>(receiptDetail, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceiptDetail(@PathVariable Long id) {
        boolean deleted = receiptDetailService.deleteReceiptDetail(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{receiptId}/details")
    public ResponseEntity<ReceiptDetail> addReceiptDetail(@PathVariable Long receiptId, @RequestBody ReceiptDetail receiptDetail) {
        ReceiptDetail createdDetail = receiptDetailService.addReceiptDetail(receiptId, receiptDetail);
        return ResponseEntity.ok(createdDetail);
    }

    @GetMapping("/{receiptId}/details")
    public ResponseEntity<List<ReceiptDetail>> getReceiptDetails(@PathVariable Long receiptId) {
        List<ReceiptDetail> receiptDetails = receiptDetailService.getReceiptDetails(receiptId);
        return ResponseEntity.ok(receiptDetails);
    }


}
