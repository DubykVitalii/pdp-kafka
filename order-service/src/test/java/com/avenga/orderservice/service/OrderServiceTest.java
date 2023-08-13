package com.avenga.orderservice.service;

import com.avenga.orderservice.TestDataConstants;
import com.avenga.orderservice.client.ProductServiceGrpcClient;
import com.avenga.orderservice.exception.NotAvailableProductException;
import com.avenga.orderservice.exception.NotEnoughProductException;
import com.avenga.orderservice.exception.OrderNotFoundException;
import com.avenga.orderservice.kafka.OrderKafkaProducer;
import com.avenga.orderservice.mapper.OrderMapper;
import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.persistence.order.Order;
import com.avenga.orderservice.model.record.NewOrderRecord;
import com.avenga.orderservice.model.record.OrderRecord;
import com.avenga.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import productservice.Product;

import java.util.Optional;

import static com.avenga.orderservice.TestDataConstants.Numbers.*;
import static com.avenga.orderservice.TestDataConstants.OrderRecords.TEST_NEW_ORDER_RECORD;
import static com.avenga.orderservice.TestDataConstants.OrderRecords.TEST_ORDER_RECORD;
import static com.avenga.orderservice.TestDataConstants.Orders.TEST_ORDER;
import static com.avenga.orderservice.TestDataConstants.Orders.TEST_ORDER_ITEMS;
import static com.avenga.orderservice.TestDataConstants.Strings.TEST_PRODUCT_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductServiceGrpcClient productServiceGrpcClient;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderKafkaProducer orderKafkaProducer;

    private NewOrderRecord newOrderRecord;
    private OrderRecord orderRecord;
    private Order order;
    private Product.ProductRecord product;

    @BeforeEach
    void setUp() {
        order = TEST_ORDER;
        order.setOrderItems(TEST_ORDER_ITEMS);
        orderRecord = TEST_ORDER_RECORD;
        newOrderRecord = TEST_NEW_ORDER_RECORD;
        product = Product.ProductRecord.newBuilder()
                .setId(TEST_ID_ONE)
                .setName(TEST_PRODUCT_NAME)
                .setDescription(TestDataConstants.Strings.TEST_PRODUCT_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setAvailable(true)
                .setAvailableProductCount(TEST_QUANTITY)
                .build();
    }

    @Test
    void createOrder_ShouldCreateOrderAndSendMessage() {
        when(orderMapper.newOrderRecordToOrder(newOrderRecord)).thenReturn(order);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderMapper.orderToOrderRecord(order)).thenReturn(orderRecord);
        when(productServiceGrpcClient.getProductById(anyLong())).thenReturn(product);

        var result = orderService.createOrder(newOrderRecord);

        assertEquals(orderRecord, result);
        verify(orderRepository, times(1)).save(order);
        verify(orderKafkaProducer, times(1)).sendOrderCreatedMessage(orderRecord);
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderRecord(order)).thenReturn(orderRecord);

        var result = orderService.getOrderById(1L);

        assertEquals(orderRecord, result);
    }

    @Test
    void getOrderById_NotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void completeOrder_ShouldCompleteOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.completeOrder(1L);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    void cancelOrder_ShouldCancelOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void createOrder_ShouldThrowNotAvailableProductException_WhenProductIsUnavailable() {
        var notAvailableProduct = product = Product.ProductRecord.newBuilder()
                .setId(TEST_ID_ONE)
                .setName(TEST_PRODUCT_NAME)
                .setDescription(TestDataConstants.Strings.TEST_PRODUCT_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setAvailable(false)
                .setAvailableProductCount(TEST_QUANTITY)
                .build();

        when(productServiceGrpcClient.getProductById(anyLong())).thenReturn(notAvailableProduct);

        assertThrows(NotAvailableProductException.class, () -> orderService.createOrder(newOrderRecord));
    }

    @Test
    void createOrder_ShouldThrowNotEnoughProductException_WhenQuantityIsInsufficient() {
        var product = Product.ProductRecord.newBuilder()
                .setId(TEST_ID_ONE)
                .setName(TEST_PRODUCT_NAME)
                .setDescription(TestDataConstants.Strings.TEST_PRODUCT_DESCRIPTION)
                .setPrice(TEST_PRICE)
                .setAvailable(true)
                .setAvailableProductCount(4)
                .build();

        when(productServiceGrpcClient.getProductById(anyLong())).thenReturn(product);

        assertThrows(NotEnoughProductException.class, () -> orderService.createOrder(newOrderRecord));
    }

    @Test
    void completeOrder_ShouldThrowOrderNotFoundException_WhenOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.completeOrder(1L));
    }

    @Test
    void cancelOrder_ShouldThrowOrderNotFoundException_WhenOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.cancelOrder(1L));
    }
}
