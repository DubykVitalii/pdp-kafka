package com.avenga.productservice.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewProductRecord(
        @NotBlank
        @JsonProperty("name") String name,

        @NotBlank
        @JsonProperty("description") String description,

        @NotNull
        @JsonProperty("price") Integer price,

        @NotNull
        @JsonProperty("available") Boolean available,

        @NotNull
        @JsonProperty("availableProductCount") Integer availableProductCount) {
}
