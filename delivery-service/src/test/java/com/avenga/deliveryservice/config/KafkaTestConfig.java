package com.avenga.deliveryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.*;
import org.testcontainers.containers.KafkaContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("test")
public class KafkaTestConfig {

    private final String bootstrapServers;

    public KafkaTestConfig(KafkaContainer kafka) {
        this.bootstrapServers = kafka.getBootstrapServers();
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put("bootstrap.servers", bootstrapServers);
        configProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put("bootstrap.servers", bootstrapServers);
        configProps.put("group.id", "test");
        configProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        configProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }
}

