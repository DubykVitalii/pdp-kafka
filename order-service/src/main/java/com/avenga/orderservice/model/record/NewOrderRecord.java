package com.avenga.orderservice.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NewOrderRecord(
        @NotNull
        @JsonProperty("customerId") Long customerId,

        @NotBlank
        @JsonProperty("deliveryAddress") String deliveryAddress,

        @NotNull
        @JsonProperty("orderItems") List<NewOrderItemRecord> orderItems) {
}
