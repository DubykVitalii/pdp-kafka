package com.avenga.orderservice.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewOrderItemRecord(
        @NotNull
        @JsonProperty("productId") Long productId,

        @NotNull
        @Min(1)
        @Max(100)
        @JsonProperty("quantity") int quantity) {
}
