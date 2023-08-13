package com.avenga.deliveryservice.repository;

import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.avenga.deliveryservice.model.persistance.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
        List<Delivery> findByStatus(DeliveryStatus status);
}
