package com.avenga.deliveryservice.model.persistance;

import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of")
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Type(JsonBinaryType.class)
    @Column(name = "`order`", columnDefinition = "jsonb")
    private Order order;
}