package com.avenga.productservice.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductKafkaMessageRecord(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("price") Integer price) {
}
