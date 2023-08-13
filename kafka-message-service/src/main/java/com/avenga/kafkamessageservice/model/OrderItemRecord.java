package com.avenga.kafkamessageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record OrderItemRecord(
        @NotNull
        @JsonProperty("id") Long id,

        @NotNull
        @JsonProperty("productDetails") ProductDetailsRecord productDetails,

        @NotNull
        @JsonProperty("quantity") int quantity) {
}
