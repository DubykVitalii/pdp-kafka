package com.avenga.kafkamessageservice.service;

import com.avenga.kafkamessageservice.kafka.KafkaProducerService;
import com.avenga.kafkamessageservice.model.OrderStatusKafkaMessageRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public void updateOrderStatus(OrderStatusKafkaMessageRecord orderStatusKafkaMessageRecord) {
        kafkaProducerService.sendOrderStatusUpdatedMessage(orderStatusKafkaMessageRecord);
     }
}
