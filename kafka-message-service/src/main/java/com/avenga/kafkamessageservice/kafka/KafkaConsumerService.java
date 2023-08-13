package com.avenga.kafkamessageservice.kafka;

import com.avenga.kafkamessageservice.model.KafkaMessageRecord;
import com.avenga.kafkamessageservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final String GROUP_ID = "kafka-service-group";
    private static final String CREATE_ORDER_TOPIC = "orders";
    private static final String CREATE_PRODUCT_TOPIC = "products";
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = CREATE_ORDER_TOPIC, groupId = GROUP_ID)
    public void listenCreateOrder(ConsumerRecord<String, String> kafkaMessage) {
        String messageValueAsJson = kafkaMessage.value();
        notificationService.sendNotificationToTopic(CREATE_ORDER_TOPIC, new KafkaMessageRecord(kafkaMessage.key(), messageValueAsJson, kafkaMessage.timestamp()));
    }

    @KafkaListener(topics = CREATE_PRODUCT_TOPIC, groupId = GROUP_ID)
    public void listenCreateProduct(ConsumerRecord<String, String> kafkaMessage) {
        String kafkaMessageValue = kafkaMessage.value();
        notificationService.sendNotificationToTopic(CREATE_PRODUCT_TOPIC, new KafkaMessageRecord(kafkaMessage.key(), kafkaMessageValue, kafkaMessage.timestamp()));
    }
}

