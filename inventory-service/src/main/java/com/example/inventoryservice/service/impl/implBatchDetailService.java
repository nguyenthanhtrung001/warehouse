package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.client.ProductClient;
import com.example.inventoryservice.dto.response.OrderQuantity;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.repository.BatchDetailRepository;
import com.example.inventoryservice.service.IBatchDetailService;
import com.example.inventoryservice.service.IBatchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Service
public class implBatchDetailService implements IBatchDetailService {
    @Autowired
    private ProductClient productClient;
    @Autowired
    private  BatchDetailRepository batchDetailRepository;
    @Autowired
    private IBatchService batchService;

    private ModelMapper modelMapper  = new ModelMapper();


    @Override
    public BatchDetail createBatchDetail(BatchDetail batchDetail) {
        return batchDetailRepository.save(batchDetail);
    }

    @Override
    public Batch getBatchByBatchDetailById(Long id) {
        BatchDetail batchDetail = batchDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BatchDetail not found with id " + id));
        return batchDetail.getBatch();
    }


    @Override
    public String getProductByBatchDetailById( Long id) {
        BatchDetail detail = getBatchDetailById(id);
        Long productId = detail.getProductId();

        try {
             String product =productClient.getNameProductByID(productId);

            return product;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public BatchDetail getBatchDetailById(Long id) {
        Optional<BatchDetail> batchDetail = batchDetailRepository.findById(id);
        return batchDetail.orElse(null);
    }

    @Override
    public Integer getQuantityByIdProductId(Long id) {
        return batchDetailRepository.getQuantityByProductId(id);
    }

    @Override
    public Long getQuantityByProductIdAndBatchId(Long productId, Long batchId) {
        return batchDetailRepository.getQuantityByProductIdAndBatchId(productId, batchId);

    }

    @Override
    public List<Long> getProductByBatchId(Long batchId) {
        return batchDetailRepository.getProductByBatchId(batchId);
    }

    @Override
    public List<BatchDetail> getBatchDetailsByProductId(Long productId) {
        return batchDetailRepository.findByProductIdAndQuantityGreaterThan(productId);
    }

    @Override
    public List<Location> getLocationByProductId(Long productId) {
        return batchDetailRepository.findLocationsByProductId(productId);
    }

    @Override
    public List<BatchDetail> getAllBatchDetails() {
        return batchDetailRepository.findAll();
    }

    @Override
    public boolean updateBatchDetail(Long id, BatchDetail batchDetail) {
        if (batchDetailRepository.existsById(id)) {
            batchDetail.setId(id);
            batchDetailRepository.save(batchDetail);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateQuantityDeliveryDetail(Long id, Integer quantity) {
        BatchDetail detail= batchDetailRepository.findById(id).orElse(null);
        if (detail != null) {
            int quantityBath = detail.getQuantity();
            if(quantity>quantityBath) return false;
            int result = quantityBath - quantity;
            System.out.println("Sl00:"+quantityBath+"--"+quantity);
            detail.setQuantity(result);
            batchDetailRepository.save(detail);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateQuantityForCheckInventory(Long id, Integer quantity) {

            BatchDetail detail= batchDetailRepository.findById(id).orElse(null);
            if (detail != null) {
                int quantityBath = detail.getQuantity();
                int result = quantityBath + quantity;
                detail.setQuantity(result);
                batchDetailRepository.save(detail);
                return true;
            }
            return false;

    }

    @Override
    public boolean updateQuantityForReturnOrder(Long id, Integer quantity) {
        BatchDetail detail= batchDetailRepository.findById(id).orElse(null);
        System.out.println("abbbbbbbc");
        if (detail != null) {
            int quantityBath = detail.getQuantity();
            System.out.println("sl cu:"+quantityBath);
            int result = quantityBath + quantity;
            System.out.println("sl moi:"+result);
            detail.setQuantity(result);
            batchDetailRepository.save(detail);
            return true;
        }
        return false;
    }



    @Override
    public List<OrderQuantity> updateQuantityForOrder(Long productId, Integer quantity) {
        List<BatchDetail> batchDetails = batchDetailRepository.findByProductIdAndQuantityGreaterThan(productId);
        Integer remainingQuantity = 0;
        List<OrderQuantity> orders = new ArrayList<>();

        if (batchDetails != null) {
            for (BatchDetail batchDetail : batchDetails) {

                OrderQuantity orderQuantity = new OrderQuantity();

                remainingQuantity = batchDetail.getQuantity() - quantity;


                if (remainingQuantity >= 0) {
                    orderQuantity.setBathDetail_Id(batchDetail.getId());
                    orderQuantity.setQuantity(quantity);
                    orders.add(orderQuantity);

                    batchDetail.setQuantity(remainingQuantity);
                    batchDetailRepository.saveAll(batchDetails);

                    return orders;
                } else {
                    orderQuantity.setBathDetail_Id(batchDetail.getId());
                    orderQuantity.setQuantity(batchDetail.getQuantity());
                    orders.add(orderQuantity);

                    batchDetail.setQuantity(0);
                    quantity = Math.abs(remainingQuantity);

                }
            }

        }
        return orders;
    }

    @Override
    public List<ProductQuantity> getTopNLowestQuantity(int limit) {
        List<ProductQuantity> allProductQuantities = batchDetailRepository.findAllOrderedByQuantity();
        return allProductQuantities.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteBatchDetailReturnBathID(List<Long>listID) {

        try{
            BatchDetail detail = batchDetailRepository.findById(listID.get(0)).orElse(null);

            for ( Long id: listID){
                System.out.println("Xóa thành công:"+id);
                batchDetailRepository.deleteById(id);
            }
            boolean tmp = batchService.deleteBatch(detail.getBatch().getId());
            if( tmp==true) System.out.println("Xóa thành công bath");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public boolean deleteBatchDetail(Long id) {
        if (batchDetailRepository.existsById(id)) {
            batchDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
