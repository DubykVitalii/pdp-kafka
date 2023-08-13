package com.avenga.kafkamessageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record KafkaMessageRecord(
        @NotNull
        @JsonProperty("key") String id,

        @NotBlank
        @JsonProperty("value") String value,

        @NotNull
        @JsonProperty("timestamp") Long timestamp) {
}
