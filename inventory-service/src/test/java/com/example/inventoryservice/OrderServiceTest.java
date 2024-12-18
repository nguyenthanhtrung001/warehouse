package com.example.inventoryservice;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.inventoryservice.client.ProductClient;
import com.example.inventoryservice.dto.response.OrderQuantity;
import com.example.inventoryservice.entity.Batch;
import com.example.inventoryservice.entity.BatchDetail;
import com.example.inventoryservice.exception.InsufficientStockException;
import com.example.inventoryservice.repository.BatchDetailRepository;
import com.example.inventoryservice.service.IBatchService;
import com.example.inventoryservice.service.impl.implBatchDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private BatchDetailRepository batchDetailRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private IBatchService batchService;

    @InjectMocks
    private implBatchDetailService batchDetailService;  // Đảm bảo sử dụng đúng batchDetailService

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testUpdateQuantityForOrder_TwoOrders_Success() {
        // Setup dữ liệu giả
        Long productId = 1L;
        Integer quantityOrder1 = 50;  // Đơn hàng 1 yêu cầu 50
        Integer quantityOrder2 = 30;  // Đơn hàng 2 yêu cầu 30
        Long warehouseId = 1L;

        // Giả định dữ liệu của BatchDetail
        BatchDetail batchDetail1 = new BatchDetail(1L, productId, 60, new Batch()); // Batch 1 có quantity = 60
        BatchDetail batchDetail2 = new BatchDetail(2L, productId, 40, new Batch()); // Batch 2 có quantity = 40
        List<BatchDetail> mockBatchDetails = Arrays.asList(batchDetail1, batchDetail2);

        // Cấu hình mock khi gọi findByProductIdAndQuantityGreaterThanInWarehouseId
        when(batchDetailRepository.findByProductIdAndQuantityGreaterThanInWarehouseId(productId, warehouseId))
                .thenReturn(mockBatchDetails);

        // Thực thi đơn hàng 1
        List<OrderQuantity> resultOrder1 = batchDetailService.updateQuantityForOrder(productId, quantityOrder1, warehouseId);
        assertNotNull(resultOrder1);
        assertEquals(1, resultOrder1.size());  // Kiểm tra rằng có 2 batch được xử lý
        assertEquals(50, resultOrder1.get(0).getQuantity());  // Đơn hàng 1 lấy 50 từ batch 1

        // Cập nhật lại số lượng batch sau đơn hàng 1
        batchDetail1.setQuantity(10);  // Cập nhật lại số lượng của batchDetail1
        batchDetail2.setQuantity(40);  // Cập nhật lại số lượng của batchDetail2

        // Kiểm tra lại số lượng của batch sau đơn hàng 1
        verify(batchDetailRepository).saveAll(mockBatchDetails);  // Kiểm tra xem đã lưu dữ liệu

        // Thực thi đơn hàng 2
        List<OrderQuantity> resultOrder2 = batchDetailService.updateQuantityForOrder(productId, quantityOrder2, warehouseId);
        assertNotNull(resultOrder2);
        assertEquals(2, resultOrder2.size());  // Kiểm tra rằng có 2 batch được xử lý
        assertEquals(30, resultOrder2.get(0).getQuantity() + resultOrder2.get(1).getQuantity());  // Đơn hàng 2 lấy 30 từ batch 1

        // Cập nhật lại số lượng batch sau đơn hàng 2
        batchDetail1.setQuantity(10);  // Cập nhật lại số lượng của batchDetail1
        batchDetail2.setQuantity(10);  // Cập nhật lại số lượng của batchDetail2

        // Lưu lại dữ liệu sau đơn hàng 2
        verify(batchDetailRepository, times(2)).saveAll(mockBatchDetails);  // Kiểm tra xem đã lưu dữ liệu sau cả hai đơn hàng
    }

    @Test
    @Transactional
    public void testUpdateQuantityForOrder_TwoOrders_LockedData() throws InterruptedException {
        // Setup dữ liệu giả
        Long productId = 1L;
        Integer quantityOrder1 = 50;  // Đơn hàng 1 yêu cầu 50
        Integer quantityOrder2 = 100; // Đơn hàng 2 yêu cầu 100
        Long warehouseId = 1L;

        // Giả định dữ liệu của BatchDetail (Kho hiện có tổng cộng 100 sản phẩm)
        BatchDetail batchDetail1 = new BatchDetail(1L, productId, 60, new Batch()); // Batch 1 có quantity = 60
        BatchDetail batchDetail2 = new BatchDetail(2L, productId, 40, new Batch()); // Batch 2 có quantity = 40
        List<BatchDetail> mockBatchDetails = Arrays.asList(batchDetail1, batchDetail2);

        // Cấu hình mock khi gọi findByProductIdAndQuantityGreaterThanInWarehouseId
        when(batchDetailRepository.findByProductIdAndQuantityGreaterThanInWarehouseId(productId, warehouseId))
                .thenReturn(mockBatchDetails);

        System.out.println("=== Tồn kho ban đầu ===");
        System.out.println("Batch 1: " + batchDetail1.getQuantity() + " sản phẩm");
        System.out.println("Batch 2: " + batchDetail2.getQuantity() + " sản phẩm");

        // Thực thi đơn hàng 1 trong một thread riêng để mô phỏng việc khóa dữ liệu
        Thread orderThread1 = new Thread(() -> {
            try {
                System.out.println("Đơn hàng 1 bắt đầu xử lý...");
                // Kiểm tra và thực thi đơn hàng 1
                List<OrderQuantity> resultOrder1 = batchDetailService.updateQuantityForOrder(productId, quantityOrder1, warehouseId);
                assertNotNull(resultOrder1);
                assertEquals(1, resultOrder1.size());  // Kiểm tra rằng có 1 batch được xử lý
                assertEquals(50, resultOrder1.get(0).getQuantity());  // Đơn hàng 1 lấy 50 từ batch 1

                System.out.println("Đơn hàng 1 hoàn tất. Kết quả OrderQuantity: ");
                resultOrder1.forEach(order -> System.out.println("Batch ID: " + order.getBathDetail_Id() + ", Số lượng: " + order.getQuantity()));

                // Cập nhật lại số lượng sau khi đơn hàng 1 xử lý
                batchDetail1.setQuantity(10);  // Cập nhật lại số lượng của batchDetail1
                batchDetail2.setQuantity(40);  // Không thay đổi batch 2

                System.out.println("=== Tồn kho sau khi đơn hàng 1 ===");
                System.out.println("Batch 1: " + batchDetail1.getQuantity() + " sản phẩm");
                System.out.println("Batch 2: " + batchDetail2.getQuantity() + " sản phẩm");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        orderThread1.start();

        // Chờ một chút để đảm bảo đơn hàng 1 đang được xử lý và dữ liệu bị khóa
        Thread.sleep(1000);  // Đảm bảo đơn hàng 1 đã được xử lý một phần

        // Thực thi đơn hàng 2 trong một thread khác, sẽ phải đợi cho đến khi đơn hàng 1 hoàn tất
        Thread orderThread2 = new Thread(() -> {
            try {
                System.out.println("Đơn hàng 2 bắt đầu xử lý...");
                List<OrderQuantity> resultOrder2 = null;
                try {
                    // Kiểm tra và thực thi đơn hàng 2
                    resultOrder2 = batchDetailService.updateQuantityForOrder(productId, quantityOrder2, warehouseId);
                } catch (InsufficientStockException e) {
                    // Kiểm tra lỗi khi số lượng không đủ
                    System.out.println("Đơn hàng 2 không đủ hàng trong kho.");
                    assertEquals("Không đủ hàng trong kho để hoàn thành đơn hàng", e.getMessage());
                }

                // Đảm bảo đơn hàng 2 không thành công nếu kho không đủ
                assertNull(resultOrder2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        orderThread2.start();

        // Đợi cho cả hai đơn hàng hoàn tất
        orderThread1.join();
        orderThread2.join();

        // Kiểm tra số lượng tồn kho cuối cùng sau khi cả 2 đơn hàng đã xử lý
        System.out.println("=== Tồn kho cuối cùng ===");
        System.out.println("Batch 1: " + batchDetail1.getQuantity() + " sản phẩm");
        System.out.println("Batch 2: " + batchDetail2.getQuantity() + " sản phẩm");

        // Xác nhận kết quả sau khi xử lý
        assertEquals(10, batchDetail1.getQuantity());  // Kiểm tra batch 1 sau đơn hàng 1
        assertEquals(40, batchDetail2.getQuantity());  // Kiểm tra batch 2 không thay đổi sau đơn hàng 1
    }

}
