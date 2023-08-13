package com.avenga.productservice.kafka;

import com.avenga.productservice.exception.JsonSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static com.avenga.productservice.TestDataConstants.Products.TEST_PRODUCT_KAFKA_MESSAGE;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@EmbeddedKafka(partitions = 1, topics = {"products"})
class ProductKafkaProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;
    private ProductKafkaProducer productKafkaProducer;

    @BeforeEach
    void setUp(EmbeddedKafkaBroker embeddedKafkaBroker) {
        var producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);

        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));
        objectMapper = mock(ObjectMapper.class);
        productKafkaProducer = new ProductKafkaProducer(objectMapper, kafkaTemplate);
    }

    @Test
    void sendProductCreatedMessage_ShouldSendSuccessfully() throws JsonProcessingException {
        var record = TEST_PRODUCT_KAFKA_MESSAGE;
        var message = "testMessage";

        when(objectMapper.writeValueAsString(record)).thenReturn(message);

        productKafkaProducer.sendProductCreatedMessage(record);

        verify(objectMapper, times(1)).writeValueAsString(record);
    }

    @Test
    void sendProductCreatedMessage_WithJsonProcessingException_ShouldThrowException() throws JsonProcessingException {
        var record = TEST_PRODUCT_KAFKA_MESSAGE;

        when(objectMapper.writeValueAsString(record)).thenThrow(new JsonProcessingException("Test Exception") {
        });

        assertThrows(JsonSerializationException.class, () -> productKafkaProducer.sendProductCreatedMessage(record));
    }
}
