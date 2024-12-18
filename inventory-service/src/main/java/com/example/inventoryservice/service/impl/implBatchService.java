package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.client.ProductClient;
import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.dto.response.BatchDetailInfo;
import com.example.inventoryservice.dto.response.BatchLocation;
import com.example.inventoryservice.dto.response.ProductQuantity;
import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.repository.BatchDetailRepository;
import com.example.inventoryservice.repository.BatchRepository;
import com.example.inventoryservice.service.IBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class implBatchService implements IBatchService {

    @Autowired
    private  BatchRepository batchRepository;
    @Autowired
    private BatchDetailRepository batchDetailRepository;
    @Autowired
    ProductClient productClient;

    @Override
    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public Batch getBatchById(Long id) {
        Optional<Batch> batch = batchRepository.findById(id);
        return batch.orElse(null);
    }

    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();

    }

    @Override
    public List<Batch> getAllBatchesForWarehouseId(Long warehouseId) {
        return batchRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public boolean updateBatch(Long id, Batch batch) {
        return false;
    }

    @Override
    public boolean deleteBatch(Long id) {
        if (batchRepository.existsById(id)) {
            batchRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public List<String> getBatchNamesByProductId(Long productId, Long warehouseId) {
        List<BatchDetail> batchDetails = batchDetailRepository.findByProductId(productId);
        return batchDetails.stream()
                .map(detail -> batchRepository.findById(detail.getBatch().getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(batch -> batch.getWarehouseId().equals(warehouseId))
                .map(Batch::getBatchName)
                .distinct()
                .collect(Collectors.toList());
    }
    public List<Long> getExpiredProductIds(Long warehouseId) {
        // Lấy ngày hiện tại
        Date currentDate = new Date();
        // Tìm tất cả các batch có expiryDate <= ngày hiện tại
        List<Batch> expiredBatches = batchRepository.findAll().stream()
                .filter(batch -> batch.getWarehouseId() == warehouseId)
                .filter(batch -> batch.getExpiryDate() != null)
                .filter(batch -> batch.getExpiryDate().compareTo(currentDate) <= 0)
                .collect(Collectors.toList());

        // Lấy danh sách các batchId từ các batch đã hết hạn
        List<Long> expiredBatchIds = expiredBatches.stream()
                .map(Batch::getId)
                .collect(Collectors.toList());

        // Lọc BatchDetail theo các batchId đã hết hạn và quantity > 0, sau đó trả về danh sách productId
        return batchDetailRepository.findAll().stream()
                .filter(batchDetail -> expiredBatchIds.contains(batchDetail.getBatch().getId()))
                .filter(batchDetail -> batchDetail.getQuantity() > 0) // Lọc theo quantity > 0
                .map(BatchDetail::getProductId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByWarehouseId(Long warehouseId) {
        // Lấy danh sách ProductQuantity theo warehouseId
        List<ProductQuantity> productQuantities = getProductQuantitiesByWarehouseId(warehouseId);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (ProductQuantity productQuantity : productQuantities) {
            try {
                // Lấy tên sản phẩm từ productClient và tạo ProductResponse
               // String productName = productClient.getNameProductByID(productQuantity.getProductId());
              ProductResponse response = productClient.getProductByID(productQuantity.getProductId());
              response.setQuantity(productQuantity.getQuantity());
              productResponses.add(response);
                //productResponses.add(new ProductResponse(productName, productQuantity.getQuantity())); // Cần thêm số lượng nếu cần
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return productResponses;
    }

    @Override
    public List<BatchDetailInfo> getBatchDetailsByWarehouseId(Long warehouseId) {
        return batchRepository.findBatchDetailsByWarehouseId(warehouseId).stream()
                .map(detailInfo -> {
                    try {
                        String name = productClient.getNameProductByID(detailInfo.getProductId());
                        detailInfo.setProductName(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Log error or handle fallback, if necessary
                    }
                    return detailInfo;
                })
                .collect(Collectors.toList());
    }



    public List<ProductQuantity> getProductQuantitiesByWarehouseId(Long warehouseId) {
        return batchDetailRepository.findProductQuantitiesByWarehouseId(warehouseId);
    }

    public List<Long> getProductIdsWithBatchesExpiringIn7Days() {
        // Lấy ngày hiện tại
        Date currentDate = new Date();

        // Tạo Calendar instance và thêm 7 ngày vào ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date sevenDaysLater = calendar.getTime();

        // Tìm tất cả các batch có expiryDate trong vòng 7 ngày tới
        List<Batch> expiringBatches = batchRepository.findAll().stream()
                .filter(batch -> !batch.getExpiryDate().before(currentDate) && batch.getExpiryDate().before(sevenDaysLater))
                .collect(Collectors.toList());

        // Lấy danh sách các batchId từ các batch sẽ hết hạn trong vòng 7 ngày
        List<Long> expiringBatchIds = expiringBatches.stream()
                .map(Batch::getId)
                .collect(Collectors.toList());

        // Lọc BatchDetail theo các batchId sẽ hết hạn và quantity > 0, sau đó trả về danh sách productId
        return batchDetailRepository.findAll().stream()
                .filter(batchDetail -> expiringBatchIds.contains(batchDetail.getBatch().getId()))
                .filter(batchDetail -> batchDetail.getQuantity() > 0) // Lọc theo quantity > 0
                .map(BatchDetail::getProductId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getWarehouseLocationsByProductId(Long productId, Long warehouseId) {
        List<BatchDetail> batchDetails = batchDetailRepository.findByProductId(productId);

        return batchDetails.stream()
                .filter(detail -> detail.getBatch().getWarehouseId().equals(warehouseId)) // Lọc theo warehouseId
                .map(detail -> Optional.ofNullable(detail.getLocation())) // Lấy Location nếu có
                .filter(Optional::isPresent) // Chỉ giữ lại các Location không null
                .map(Optional::get) // Lấy giá trị Location từ Optional
                .map(Location::getWarehouseLocation) // Lấy warehouseLocation từ Location
                .distinct() // Loại bỏ các giá trị trùng lặp
                .collect(Collectors.toList()); // Chuyển kết quả thành List
    }


    @Override
    public BatchLocation getBatchLocatonForProduct(Long productId, Long warehouseId){

        List<String> batchNames = getBatchNamesByProductId(productId,warehouseId );
        List<String> warehouseLocations = getWarehouseLocationsByProductId(productId,warehouseId);


        String batchNamesStr = String.join(", ", batchNames);

        String warehouseLocationsStr = String.join(", ", warehouseLocations);

        return new BatchLocation( warehouseLocationsStr,batchNamesStr);


    }


}