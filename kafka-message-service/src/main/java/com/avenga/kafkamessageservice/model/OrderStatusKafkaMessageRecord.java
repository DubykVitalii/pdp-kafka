package com.avenga.kafkamessageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderStatusKafkaMessageRecord(
        @NotBlank
        @JsonProperty("id") Long id,

        @NotNull
        @JsonProperty("status") OrderStatus status) {
}
