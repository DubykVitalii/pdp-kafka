package com.avenga.deliveryservice.model.persistance;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of")
public class ProductDetails {
    private Long id;
    private String name;
    private Integer price;
}
