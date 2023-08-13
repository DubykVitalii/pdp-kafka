package com.avenga.orderservice.model.record;

import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record OrderRecord(
        @NotNull
        @JsonProperty("id") Long id,

        @NotNull
        @JsonProperty("createdAt") LocalDate createdAt,

        @NotNull
        @JsonProperty("customerId") Long customerId,

        @NotBlank
        @JsonProperty("deliveryAddress") String deliveryAddress,

        @NotNull
        @JsonProperty("orderItems") List<OrderItemRecord> orderItems,

        @NotNull
        @JsonProperty("status") OrderStatus status) {
}
