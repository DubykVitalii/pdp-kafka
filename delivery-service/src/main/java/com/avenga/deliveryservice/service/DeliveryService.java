package com.avenga.deliveryservice.service;

import com.avenga.deliveryservice.exception.DeliveryNotFoundException;
import com.avenga.deliveryservice.kafka.KafkaProducerService;
import com.avenga.deliveryservice.mapper.DeliveryMapper;
import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.avenga.deliveryservice.model.enumeration.OrderStatus;
import com.avenga.deliveryservice.model.kafka.OrderStatusKafkaMessageRecord;
import com.avenga.deliveryservice.model.persistance.Delivery;
import com.avenga.deliveryservice.model.persistance.Order;
import com.avenga.deliveryservice.model.record.DeliveryRecord;
import com.avenga.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final KafkaProducerService kafkaProducerService;

    public List<DeliveryRecord> getAllDelivery(DeliveryStatus status){
        if (status == null) {
            return deliveryRepository.findAll().stream()
                    .map(deliveryMapper::toDeliveryRecord)
                    .collect(Collectors.toList());
        }
        return deliveryRepository.findByStatus(status).stream()
                .map(deliveryMapper::toDeliveryRecord)
                .collect(Collectors.toList());
    }

    public DeliveryRecord getDeliveryById(Long id){
        var delivery = deliveryRepository.findById(id).orElseThrow(()->new DeliveryNotFoundException(id));
        return deliveryMapper.toDeliveryRecord(delivery);
    }

    public void createDelivery(Order order){
        var delivery = Delivery.of()
                .status(DeliveryStatus.PENDING)
                .order(order)
                .build();
        deliveryRepository.save(delivery);
    }

    public void updateDeliveryStatusById(Long deliveryId, DeliveryStatus status){
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(()->new DeliveryNotFoundException(deliveryId));
        delivery.setStatus(status);
        deliveryRepository.save(delivery);

        if(status.equals(DeliveryStatus.DELIVERED)) {
            kafkaProducerService.sendOrderStatusUpdatedMessage(new OrderStatusKafkaMessageRecord(delivery.getOrder().getId(), OrderStatus.COMPLETED));
        } else if(status.equals(DeliveryStatus.CANCELED)) {
            kafkaProducerService.sendOrderStatusUpdatedMessage(new OrderStatusKafkaMessageRecord(delivery.getOrder().getId(), OrderStatus.CANCELED));
        }
    }
}
