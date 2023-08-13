package com.avenga.productservice.kafka;

import com.avenga.productservice.exception.JsonSerializationException;
import com.avenga.productservice.model.record.ProductKafkaMessageRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductKafkaProducer {

    private static final String TOPIC = "products";
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendProductCreatedMessage(ProductKafkaMessageRecord productKafkaMessageRecord) {
        var message = toMessage(productKafkaMessageRecord);
        kafkaTemplate.send(TOPIC, message);
    }

    private String toMessage(ProductKafkaMessageRecord productKafkaMessageRecord) {
        try {
            return objectMapper.writeValueAsString(productKafkaMessageRecord);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Error serializing ProductKafkaMessageRecord to JSON", e);
        }
    }
}
