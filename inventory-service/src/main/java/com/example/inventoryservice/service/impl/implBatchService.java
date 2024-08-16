package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.dto.response.BatchLocation;
import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.entity.Location;
import com.example.inventoryservice.repository.BatchDetailRepository;
import com.example.inventoryservice.repository.BatchRepository;
import com.example.inventoryservice.service.IBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class implBatchService implements IBatchService {

    @Autowired
    private  BatchRepository batchRepository;
    @Autowired
    private BatchDetailRepository batchDetailRepository;


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


    public List<String> getBatchNamesByProductId(Long productId) {
        List<BatchDetail> batchDetails = batchDetailRepository.findByProductId(productId);
        return batchDetails.stream()
                .map(detail -> batchRepository.findById(detail.getBatch().getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Batch::getBatchName)
                .distinct()
                .collect(Collectors.toList());
    }
    public List<Long> getExpiredProductIds() {
        // Lấy ngày hiện tại
        Date currentDate = new Date();
        // Tìm tất cả các batch có expiryDate <= ngày hiện tại
        List<Batch> expiredBatches = batchRepository.findAll().stream()
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

    public List<String> getWarehouseLocationsByProductId(Long productId) {
        List<BatchDetail> batchDetails = batchDetailRepository.findByProductId(productId);
        return batchDetails.stream()
                .map(detail -> Optional.ofNullable(detail.getLocation()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Location::getWarehouseLocation)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public BatchLocation getBatchLocatonForProduct(Long productId){

        List<String> batchNames = getBatchNamesByProductId(productId);
        List<String> warehouseLocations = getWarehouseLocationsByProductId(productId);


        String batchNamesStr = String.join(", ", batchNames);

        String warehouseLocationsStr = String.join(", ", warehouseLocations);

        return new BatchLocation( warehouseLocationsStr,batchNamesStr);


    }


}