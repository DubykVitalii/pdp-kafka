package com.avenga.deliveryservice.kafka;

import com.avenga.deliveryservice.model.enumeration.OrderStatus;
import com.avenga.deliveryservice.model.kafka.OrderStatusKafkaMessageRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static com.avenga.deliveryservice.TestDataConstants.Numbers.TEST_ID_ONE;
import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOrderStatusUpdatedMessage() throws JsonProcessingException {
        var messageRecord = new OrderStatusKafkaMessageRecord(TEST_ID_ONE, OrderStatus.COMPLETED);
        var message = "message";
        when(objectMapper.writeValueAsString(messageRecord)).thenReturn(message);

        kafkaProducerService.sendOrderStatusUpdatedMessage(messageRecord);

        verify(objectMapper, times(1)).writeValueAsString(messageRecord);
        verify(kafkaTemplate, times(1)).send("orders-status", message);
    }
}