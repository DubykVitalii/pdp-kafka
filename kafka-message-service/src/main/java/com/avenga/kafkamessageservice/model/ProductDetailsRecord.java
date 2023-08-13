package com.avenga.kafkamessageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDetailsRecord(
        @NotNull
        @JsonProperty("id") Long id,

        @NotBlank
        @JsonProperty("name") String name,

        @NotNull
        @JsonProperty("price") Integer price) {
}
