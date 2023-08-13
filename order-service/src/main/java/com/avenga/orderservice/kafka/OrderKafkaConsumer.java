package com.avenga.orderservice.kafka;

import com.avenga.orderservice.exception.JsonSerializationException;
import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.record.OrderStatusKafkaMessageRecord;
import com.avenga.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderKafkaConsumer {

    private static final String UPDATE_ORDER_STATUS_TOPIC = "orders-status";
    private static final String GROUP_ID = "order-service-group";
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = UPDATE_ORDER_STATUS_TOPIC, groupId = GROUP_ID)
    public void listenUpdateOrder(ConsumerRecord<String, String> kafkaMessage) {
        OrderStatusKafkaMessageRecord orderStatusKafkaMessageRecord = convertToRecord(kafkaMessage.value());
        if (orderStatusKafkaMessageRecord.status().equals(OrderStatus.COMPLETED)) {
            orderService.completeOrder(orderStatusKafkaMessageRecord.id());
        } else {
            orderService.cancelOrder(orderStatusKafkaMessageRecord.id());
        }
    }

    private OrderStatusKafkaMessageRecord convertToRecord(String value) {
        try {
            return objectMapper.readValue(value, OrderStatusKafkaMessageRecord.class);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Error serializing ", e);
        }
    }
}

