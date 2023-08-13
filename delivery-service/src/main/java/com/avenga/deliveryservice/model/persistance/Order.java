package com.avenga.deliveryservice.model.persistance;

import com.avenga.deliveryservice.model.enumeration.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of")
public class Order {
    private Long id;
    private LocalDate createdAt;
    private Long customerId;
    private String deliveryAddress;
    private List<OrderItem> orderItems;
    private OrderStatus status;
}
