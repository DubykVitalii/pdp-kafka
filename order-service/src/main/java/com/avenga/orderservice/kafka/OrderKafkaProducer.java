package com.avenga.orderservice.kafka;

import com.avenga.orderservice.exception.JsonSerializationException;
import com.avenga.orderservice.model.record.OrderRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderKafkaProducer {

    private static final String CREATE_ORDER_TOPIC = "orders";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendOrderCreatedMessage(OrderRecord orderRecord) {
        String orderRecordAsJson = convertToJson(orderRecord);
        kafkaTemplate.send(CREATE_ORDER_TOPIC, orderRecordAsJson);
    }

    private String convertToJson(OrderRecord orderRecord) {
        try {
            return objectMapper.writeValueAsString(orderRecord);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Error serializing", e);
        }
    }
}
