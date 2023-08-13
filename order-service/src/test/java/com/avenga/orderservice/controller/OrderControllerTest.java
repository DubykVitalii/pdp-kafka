package com.avenga.orderservice.controller;

import com.avenga.orderservice.TestDataConstants;
import com.avenga.orderservice.client.ProductServiceGrpcClient;
import com.avenga.orderservice.config.KafkaTestConfig;
import com.avenga.orderservice.config.TestContainersConfig;
import com.avenga.orderservice.exception.ExceptionMessage;
import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.persistence.order.Order;
import com.avenga.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import productservice.Product;

import java.util.stream.Collectors;

import static com.avenga.orderservice.TestDataConstants.Numbers.*;
import static com.avenga.orderservice.TestDataConstants.OrderRecords.TEST_NEW_ORDER_RECORD;
import static com.avenga.orderservice.TestDataConstants.Orders.TEST_ORDER;
import static com.avenga.orderservice.TestDataConstants.Orders.TEST_ORDER_ITEMS;
import static com.avenga.orderservice.TestDataConstants.Strings.TEST_PRODUCT_NAME;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@Import({TestContainersConfig.class, KafkaTestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {

    @MockBean
    private ProductServiceGrpcClient productServiceGrpcClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    private Order order;

    @BeforeAll
    public void setup() {
        order = TEST_ORDER;
        order.setOrderItems(TEST_ORDER_ITEMS);
        orderRepository.saveAndFlush(order);
    }

    @AfterAll
    public void cleanup() {
        orderRepository.deleteAll();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void getOrderById() throws Exception {
        mockMvc.perform(get("/api/v1/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.status").value(order.getStatus().toString()))
                .andExpect(jsonPath("$.customerId").value(order.getCustomerId()))
                .andExpect(jsonPath("$.deliveryAddress").value(order.getDeliveryAddress()));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void findAllOrdersByStatus() throws Exception {
        mockMvc.perform(get("/api/v1/orders").param("status", OrderStatus.IN_PROGRESS.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(OrderStatus.IN_PROGRESS.toString()));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void findAllOrdersByCustomerId() throws Exception {
        mockMvc.perform(get("/api/v1/orders/user/" + TEST_ID_ONE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(TEST_ID_ONE));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void createOrder() throws Exception {
        var newOrderRecord = TEST_NEW_ORDER_RECORD;
        var orderItemsJson = newOrderRecord.orderItems().stream()
                .map(item -> """
                        {
                          "productId": %d,
                          "quantity": %d
                        }
                        """.formatted(item.productId(), item.quantity()))
                .collect(Collectors.joining(", "));

        var json = """
                {
                  "customerId": %d,
                  "deliveryAddress": "%s",
                  "orderItems": [%s]
                }
                """.formatted(newOrderRecord.customerId(), newOrderRecord.deliveryAddress(), orderItemsJson);

        when(productServiceGrpcClient.getProductById(anyLong())).thenReturn(
                Product.ProductRecord.newBuilder()
                        .setId(TEST_ID_ONE)
                        .setName(TEST_PRODUCT_NAME)
                        .setDescription(TestDataConstants.Strings.TEST_PRODUCT_DESCRIPTION)
                        .setPrice(TEST_PRICE)
                        .setAvailable(true)
                        .setAvailableProductCount(TEST_QUANTITY)
                        .build());

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void completeOrder() throws Exception {
        mockMvc.perform(put("/api/v1/orders/complete/" + order.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.COMPLETED.toString()));
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void cancelOrder() throws Exception {
        mockMvc.perform(put("/api/v1/orders/cancel/" + order.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.CANCELED.toString()));
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void getOrderById_NotFound() throws Exception {
        var nonExistentId = 9999L;
        mockMvc.perform(get("/api/v1/orders/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ExceptionMessage.ORDER_WITH_ID_NOT_FOUND.formatted(nonExistentId)));
    }
}

