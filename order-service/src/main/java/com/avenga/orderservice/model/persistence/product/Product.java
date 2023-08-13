package com.avenga.orderservice.model.persistence.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;

    private String name;

    private String description;

    private Integer price;

    private boolean available;

    private int availableProductCount;
}
