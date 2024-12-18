package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.client.ProductClient;
import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.dto.response.BatchDetailDTO;
import com.example.inventoryservice.dto.response.OrderQuantity;
import com.example.inventoryservice.dto.response.ProductLocation;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.exception.InsufficientStockException;
import com.example.inventoryservice.repository.BatchDetailRepository;
import com.example.inventoryservice.service.IBatchDetailService;
import com.example.inventoryservice.service.IBatchService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class implBatchDetailService implements IBatchDetailService {
    @Autowired
    private ProductClient productClient;
    @Autowired
    private  BatchDetailRepository batchDetailRepository;
    @Autowired
    private IBatchService batchService;

    private ModelMapper modelMapper  = new ModelMapper();
    @PersistenceContext
    private EntityManager entityManager;


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
    public Integer getQuantityByIdProductId(Long productId, Long warehouseId) {
        return batchDetailRepository.getQuantityByProductIdWarehouse(productId, warehouseId);
    }
    @Transactional
    public Integer getQuantityByIdProductId_lock(Long productId, Long warehouseId) {
        return batchDetailRepository.getQuantityByProductIdWarehouse_lock(productId, warehouseId);
       // return  getQuantityWithLockTimeout(productId,warehouseId);
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
    public List<BatchDetail> getBatchDetailsByProductId(Long productId, Long warehouseId) {
        return batchDetailRepository.findByProductIdAndQuantityGreaterThan(productId,warehouseId);
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

        return false;
    }

    @Transactional
    public BatchDetail updateBatchDetailForProductWithLocation(Long productId, Long locationId) {
        BatchDetail detail = batchDetailRepository.findById(productId).orElse(null);
        if (detail!= null ){
            detail.setLocation(new Location(locationId));
            batchDetailRepository.save(detail);
            return detail;
        }
        return null;
    }

    @Transactional
    public boolean updateQuantityDeliveryDetail(Long id, Integer quantity) {
        // lock db
        BatchDetail detail= batchDetailRepository.findBatchDetailForUpdate(id);
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

    @Transactional
    public boolean updateQuantityForCheckInventory(Long id, Integer quantity) {
            // lock db
            BatchDetail detail= batchDetailRepository.findBatchDetailForUpdate(id);
            if (detail != null) {
                int quantityBath = detail.getQuantity();
                int result = quantityBath + quantity;
                detail.setQuantity(result);
                batchDetailRepository.save(detail);
                return true;
            }
            return false;

    }

    @Transactional
    public boolean updateQuantityForReturnOrder(Long id, Integer quantity) {
        // lock db
        BatchDetail detail= batchDetailRepository.findBatchDetailForUpdate(id);
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



    @Transactional
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

    @Transactional
    public List<OrderQuantity> updateQuantityForOrder(Long productId, Integer quantity, Long warehouseId) {
        // Lấy danh sách BatchDetail đã khóa (Pessimistic Locking)
        List<BatchDetail> batchDetails = batchDetailRepository.findByProductIdAndQuantityGreaterThanInWarehouseId(productId, warehouseId);
        Integer remainingQuantity = quantity; // Dùng quantity ban đầu
        List<OrderQuantity> orders = new ArrayList<>();

        if (batchDetails != null && !batchDetails.isEmpty()) {
            for (BatchDetail batchDetail : batchDetails) {
                OrderQuantity orderQuantity = new OrderQuantity();

                if (remainingQuantity > 0) {
                    if (batchDetail.getQuantity() >= remainingQuantity) {
                        // Nếu batch có đủ số lượng, xuất hàng
                        orderQuantity.setBathDetail_Id(batchDetail.getId());
                        orderQuantity.setQuantity(remainingQuantity);
                        orders.add(orderQuantity);

                        // Cập nhật tồn kho của batch
                        batchDetail.setQuantity(batchDetail.getQuantity() - remainingQuantity);
                        remainingQuantity = 0;  // Đã hoàn thành xuất hàng

                    } else {
                        // Nếu batch không đủ, xuất hết và cập nhật số lượng còn thiếu
                        orderQuantity.setBathDetail_Id(batchDetail.getId());
                        orderQuantity.setQuantity(batchDetail.getQuantity());
                        orders.add(orderQuantity);

                        remainingQuantity -= batchDetail.getQuantity();
                        batchDetail.setQuantity(0); // Đặt số lượng của batch về 0
                    }
                }

                // Nếu đã xuất đủ số lượng, thoát khỏi vòng lặp
                if (remainingQuantity == 0) {
                    break;
                }
            }
//            try {
//                // Chuyển đổi giây thành mili giây và gọi Thread.sleep
//                Thread.sleep(15 * 1000);
//            } catch (InterruptedException e) {
//                // Xử lý ngoại lệ nếu luồng bị gián đoạn
//                Thread.currentThread().interrupt();
//                System.out.println("The thread was interrupted.");
//            }

            // Lưu tất cả thay đổi sau khi hoàn tất quá trình cập nhật

            batchDetailRepository.saveAll(batchDetails);

            if (remainingQuantity > 0) {
                // Nếu vẫn còn số lượng cần xuất, báo lỗi
                throw new InsufficientStockException("Không đủ hàng trong kho để hoàn thành đơn hàng");
            }
        } else {
            throw new InsufficientStockException("Không tìm thấy thông tin sản phẩm trong kho");
        }
        System.out.println("OrderQuantities after update: " + orders);
        return orders;
    }

    @Override
    public List<ProductQuantity> getTopNLowestQuantity(int limit, Long warehouseId) {
        List<ProductQuantity> allProductQuantities = batchDetailRepository.findAllInventoryProductAndQuantityForWarehouse(warehouseId);
        return allProductQuantities.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Transactional
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


    @Transactional
    public boolean deleteBatchDetail(Long id) {
        if (batchDetailRepository.existsById(id)) {
            batchDetailRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<BatchDetailDTO> getBatchDetailsByBatchId(Long batchId) {
        return batchDetailRepository.findByBatch_Id(batchId).stream()
                .map(detail -> {
                    // Gọi API để lấy ProductResponse
                    ProductResponse product = productClient.getProductByID(detail.getProductId());

                    // Tạo DTO
                    return new BatchDetailDTO(
                            detail.getId(),
                            product, // Đối tượng product được lấy từ client
                            detail.getQuantity(),
                            detail.getLocation()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductLocation> getBatchDetailsByLocationId(Long locationId) {
        List<ProductQuantity> productQuantities = batchDetailRepository.findSumQuantityByProductIdAndLocationId(locationId);
        List<ProductLocation> productLocations = new ArrayList<>();

        productQuantities.forEach(productQuantity -> {
            try {
                ProductResponse product = productClient.getProductByID(productQuantity.getProductId());
                productLocations.add(new ProductLocation(product, productQuantity.getQuantity()));
            } catch (Exception e) {
                System.err.println("Error fetching product details for productId: " + productQuantity.getProductId());
                e.printStackTrace();
            }
        });

        return productLocations;
    }

    @Override
    public Long getTotalQuantityByWarehouseAndLocation(Long warehouseId, Long locationId) {
        Long totalQuantity = batchDetailRepository.findTotalQuantityByWarehouseAndLocation(locationId,warehouseId);
        return  totalQuantity;
    }

}
