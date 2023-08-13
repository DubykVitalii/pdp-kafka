package com.avenga.deliveryservice.model.record;

import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.avenga.deliveryservice.model.persistance.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DeliveryRecord(
        @NotNull
        @JsonProperty("id") Long id,

        @NotNull
        @JsonProperty("status") DeliveryStatus status,

        @JsonProperty("order") Order order) {
}
