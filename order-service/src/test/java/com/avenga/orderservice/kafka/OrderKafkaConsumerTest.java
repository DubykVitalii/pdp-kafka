package com.avenga.orderservice.kafka;

import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.record.OrderStatusKafkaMessageRecord;
import com.avenga.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static com.avenga.orderservice.TestDataConstants.Numbers.TEST_ID_ONE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderKafkaConsumerTest {
    private static final String UPDATE_ORDER_STATUS_TOPIC = "orders-status";

    @InjectMocks
    private OrderKafkaConsumer orderKafkaConsumer;

    @Mock
    private OrderService orderService;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void shouldCompleteOrderWhenStatusCompleted() throws JsonProcessingException {
        var messageValue = "{\"id\":" + TEST_ID_ONE + ",\"status\":\"COMPLETED\"}";
        var orderStatusKafkaMessageRecord = new OrderStatusKafkaMessageRecord(TEST_ID_ONE, OrderStatus.COMPLETED);
        when(objectMapper.readValue(messageValue, OrderStatusKafkaMessageRecord.class)).thenReturn(orderStatusKafkaMessageRecord);

        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>(UPDATE_ORDER_STATUS_TOPIC, 0, 0, null, messageValue);

        orderKafkaConsumer.listenUpdateOrder(consumerRecord);

        verify(orderService).completeOrder(TEST_ID_ONE);
    }

    @Test
    void shouldCancelOrderWhenStatusCanceled() throws JsonProcessingException {
        var messageValue = "{\"id\":" + TEST_ID_ONE + ",\"status\":\"CANCELED\"}";
        var orderStatusKafkaMessageRecord = new OrderStatusKafkaMessageRecord(TEST_ID_ONE, OrderStatus.CANCELED);
        when(objectMapper.readValue(messageValue, OrderStatusKafkaMessageRecord.class)).thenReturn(orderStatusKafkaMessageRecord);

        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>(UPDATE_ORDER_STATUS_TOPIC, 0, 0, null, messageValue);

        orderKafkaConsumer.listenUpdateOrder(consumerRecord);

        verify(orderService).cancelOrder(TEST_ID_ONE);
    }
}
