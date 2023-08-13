package com.avenga.orderservice.kafka;

import com.avenga.orderservice.TestDataConstants;
import com.avenga.orderservice.exception.JsonSerializationException;
import com.avenga.orderservice.model.record.OrderRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderKafkaProducerTest {
    private static final String CREATE_ORDER_TOPIC = "orders";

    @InjectMocks
    private OrderKafkaProducer orderKafkaProducer;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void shouldSendOrderCreatedMessage() throws JsonProcessingException {
        var orderRecord = TestDataConstants.OrderRecords.TEST_ORDER_RECORD;
        var json = convertToJson(orderRecord);

        when(objectMapper.writeValueAsString(orderRecord)).thenReturn(json);

        orderKafkaProducer.sendOrderCreatedMessage(TestDataConstants.OrderRecords.TEST_ORDER_RECORD);

        verify(kafkaTemplate).send(CREATE_ORDER_TOPIC, json);
    }

    private String convertToJson(OrderRecord orderRecord) {
        try {
            return objectMapper.writeValueAsString(orderRecord);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Error serializing", e);
        }
    }
}

