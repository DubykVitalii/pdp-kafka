package com.avenga.deliveryservice.kafka;

import com.avenga.deliveryservice.exception.JsonSerializationException;
import com.avenga.deliveryservice.model.kafka.OrderStatusKafkaMessageRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String UPDATE_ORDER_STATUS_TOPIC = "orders-status";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendOrderStatusUpdatedMessage(OrderStatusKafkaMessageRecord kafkaMessage) {
        String recordAsJson = convertToJson(kafkaMessage);
        kafkaTemplate.send(UPDATE_ORDER_STATUS_TOPIC, recordAsJson);
    }

    private String convertToJson(OrderStatusKafkaMessageRecord kafkaMessage) {
        try {
            return objectMapper.writeValueAsString(kafkaMessage);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Error serializing", e);
        }
    }
}

