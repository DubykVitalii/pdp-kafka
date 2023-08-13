package com.avenga.deliveryservice.kafka;

import com.avenga.deliveryservice.model.persistance.Order;
import com.avenga.deliveryservice.service.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.avenga.deliveryservice.TestDataConstants.Orders.TEST_ORDER;
import static org.mockito.Mockito.*;

class KafkaConsumerServiceTest {

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListenCreateOrder() throws Exception {
        var record = new ConsumerRecord<>("orders", 0, 0L, "key", "value");
        when(objectMapper.readValue(record.value(), Order.class)).thenReturn(TEST_ORDER);

        kafkaConsumerService.listenCreateOrder(record);

        verify(objectMapper, times(1)).readValue(record.value(), Order.class);
        verify(deliveryService, times(1)).createDelivery(TEST_ORDER);
    }
}
