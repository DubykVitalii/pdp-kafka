package com.avenga.deliveryservice.kafka;

import com.avenga.deliveryservice.model.persistance.Order;
import com.avenga.deliveryservice.service.DeliveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final String GROUP_ID = "delivery-service-group";
    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;
    private static final String CREATE_ORDER_TOPIC = "orders";

    @KafkaListener(topics = CREATE_ORDER_TOPIC, groupId = GROUP_ID)
    public void listenCreateOrder(ConsumerRecord<String, String> kafkaMessage) {
        String messageValueAsJson = kafkaMessage.value();
        try {
            Order order = objectMapper.readValue(messageValueAsJson, Order.class);
            deliveryService.createDelivery(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

