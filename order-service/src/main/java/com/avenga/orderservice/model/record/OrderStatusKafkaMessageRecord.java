package com.avenga.orderservice.model.record;

import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record OrderStatusKafkaMessageRecord(
        @NotNull
        @JsonProperty("id") Long id,

        @NotNull
        @JsonProperty("status") OrderStatus status) {
}
