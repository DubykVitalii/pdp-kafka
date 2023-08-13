package com.avenga.deliveryservice.controller;

import com.avenga.deliveryservice.config.KafkaTestConfig;
import com.avenga.deliveryservice.config.TestContainersConfig;
import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.avenga.deliveryservice.model.persistance.Delivery;
import com.avenga.deliveryservice.repository.DeliveryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.avenga.deliveryservice.TestDataConstants.Deliveries.TEST_DELIVERY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class DeliveryControllerTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private MockMvc mockMvc;

    private Delivery delivery;

    @BeforeAll
    public void setup() {
        delivery = TEST_DELIVERY;
        deliveryRepository.save(delivery);
    }

    @AfterAll
    public void cleanup() {
        deliveryRepository.deleteAll();
    }

    @Test
    @Order(2)
    void getAllDeliveries() throws Exception {
        mockMvc.perform(get("/api/v1/deliveries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Order(2)
    void getDeliveryById() throws Exception {
        mockMvc.perform(get("/api/v1/deliveries/" + delivery.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(delivery.getId()))
                .andExpect(jsonPath("$.status").value(DeliveryStatus.PENDING.toString()));

    }

    @Test
    @Order(3)
    void updateDeliveryStatusById() throws Exception {
        var status = DeliveryStatus.DELIVERED;

        mockMvc.perform(put("/api/v1/deliveries/" + delivery.getId() + "/status")
                        .param("status", status.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/deliveries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(DeliveryStatus.DELIVERED.toString()));
    }
}
