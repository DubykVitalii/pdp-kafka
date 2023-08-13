package com.avenga.deliveryservice.model.persistance;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of")
public class OrderItem {
    private Long id;
    private ProductDetails productDetails;
    private int quantity;
}
