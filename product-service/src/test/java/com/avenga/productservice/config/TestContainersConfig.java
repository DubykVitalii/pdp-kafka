package com.avenga.productservice.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
@Profile("test")
public class TestContainersConfig {

    @Bean
    public KafkaContainer kafkaContainer() {
        var kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafkaContainer.start();
        return kafkaContainer;
    }

    @PreDestroy
    public void stopContainers() {
        kafkaContainer().stop();
    }
}
