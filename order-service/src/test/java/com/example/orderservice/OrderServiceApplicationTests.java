package com.example.orderservice;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.*;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.InvoiceDetailRequest;
import com.example.orderservice.dto.InvoiceRequest;
import com.example.orderservice.dto.response.ProductQuantity;
import com.example.orderservice.entity.ContactInfo;
import com.example.orderservice.entity.Customer;
import com.example.orderservice.entity.Invoice;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.InvoiceDetailRepository;
import com.example.orderservice.repository.InvoiceRepository;
import com.example.orderservice.service.IContactInfoService;
import com.example.orderservice.service.IInvoiceService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceApplicationTests {

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private IContactInfoService contactInfoService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    @InjectMocks
    private IInvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoiceWithConcurrentTransactions() throws InterruptedException, ExecutionException {
        // Mocking the behavior of inventoryClient for sufficient stock
        when(inventoryClient.getQuantityByProductIdAndWarehouse_lock(1L, 1L)).thenReturn(10); // 10 available items

        // Mocking other dependencies
        Customer customer = new Customer();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(contactInfoService.getContactInfoById(anyLong())).thenReturn(new ContactInfo());

        // Simulating an InvoiceRequest
        InvoiceRequest orderRequest = new InvoiceRequest();
        orderRequest.setCustomer(1L);
        orderRequest.setWarehouseId(1L);
        orderRequest.setPrice(1000L);
        orderRequest.setEmployeeId(1L);
        orderRequest.setOrder_Details(Collections.singletonList(new InvoiceDetailRequest(1L, 5L, 100))); // 5 products

        // Executor for simulating concurrent requests
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Runnable task to create invoice
        Runnable task = () -> {
            try {
                Invoice result = invoiceService.createInvoice(orderRequest);
                assertNotNull(result); // Ensure the invoice is created successfully
            } catch (IllegalArgumentException e) {
                fail("Transaction failed unexpectedly: " + e.getMessage()); // This block should not be reached
            }
        };

        // Submit 2 transactions to the executor
        Future<?> future1 = executor.submit(task);
        Future<?> future2 = executor.submit(task);

        // Wait for both tasks to finish
        future1.get();
        future2.get();

        // Verify the number of times the lock method is called (should be twice for both transactions)
        verify(inventoryClient, times(2)).getQuantityByProductIdAndWarehouse_lock(1L, 1L);

        executor.shutdown();
    }

    @Test
    void testCreateInvoiceWithInsufficientStock() throws InterruptedException, ExecutionException {
        // Mocking the behavior of inventoryClient for insufficient stock
        when(inventoryClient.getQuantityByProductIdAndWarehouse_lock(1L, 1L)).thenReturn(3); // 3 available items

        // Mocking other dependencies
        Customer customer = new Customer();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(contactInfoService.getContactInfoById(anyLong())).thenReturn(new ContactInfo());

        // Simulating an InvoiceRequest
        InvoiceRequest orderRequest = new InvoiceRequest();
        orderRequest.setCustomer(1L);
        orderRequest.setWarehouseId(1L);
        orderRequest.setPrice(1000L);
        orderRequest.setEmployeeId(1L);
        orderRequest.setOrder_Details(Collections.singletonList(new InvoiceDetailRequest(1L, 5L, 100))); // 5 products

        // Executor for simulating concurrent requests
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Runnable task to create invoice
        Runnable task = () -> {
            try {
                invoiceService.createInvoice(orderRequest);
                fail("Expected IllegalArgumentException due to insufficient stock"); // Expected exception, transaction should fail
            } catch (IllegalArgumentException e) {
                assertTrue(e.getMessage().contains("Tồn kho không đủ")); // Check if the exception message contains the expected error
            }
        };

        // Submit 2 transactions to the executor
        Future<?> future1 = executor.submit(task);
        Future<?> future2 = executor.submit(task);

        // Wait for both tasks to finish
        future1.get();
        future2.get();

        executor.shutdown();
    }
}
