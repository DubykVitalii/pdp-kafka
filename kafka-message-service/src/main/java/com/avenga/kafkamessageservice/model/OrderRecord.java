package com.avenga.kafkamessageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRecord(
        @NotBlank
        @JsonProperty("id") Long id,

        @NotNull
        @JsonProperty("customerId") Long customerId,

        @NotBlank
        @JsonProperty("deliveryAddress") String deliveryAddress,

        @NotNull
        @JsonProperty("orderItems") List<OrderItemRecord> orderItems,

        @NotNull
        @JsonProperty("status") OrderStatus status) {
}
