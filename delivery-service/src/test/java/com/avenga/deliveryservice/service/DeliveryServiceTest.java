package com.avenga.deliveryservice.service;

import com.avenga.deliveryservice.exception.DeliveryNotFoundException;
import com.avenga.deliveryservice.kafka.KafkaProducerService;
import com.avenga.deliveryservice.mapper.DeliveryMapper;
import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.avenga.deliveryservice.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.avenga.deliveryservice.TestDataConstants.Deliveries.TEST_DELIVERY;
import static com.avenga.deliveryservice.TestDataConstants.Numbers.TEST_ID_ONE;
import static org.mockito.Mockito.*;

class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDeliveryWithStatus() {
        var deliveries = List.of(TEST_DELIVERY);
        when(deliveryRepository.findByStatus(DeliveryStatus.PENDING)).thenReturn(deliveries);

        deliveryService.getAllDelivery(DeliveryStatus.PENDING);

        verify(deliveryRepository, times(1)).findByStatus(DeliveryStatus.PENDING);
        verify(deliveryMapper, times(1)).toDeliveryRecord(any());
    }

    @Test
    void getAllDeliveryWithoutStatus() {
        var deliveries = List.of(TEST_DELIVERY);
        when(deliveryRepository.findAll()).thenReturn(deliveries);

        deliveryService.getAllDelivery(null);

        verify(deliveryRepository, times(1)).findAll();
        verify(deliveryMapper, times(1)).toDeliveryRecord(any());
    }

    @Test
    void getDeliveryById() {
        when(deliveryRepository.findById(TEST_ID_ONE)).thenReturn(Optional.of(TEST_DELIVERY));

        deliveryService.getDeliveryById(TEST_ID_ONE);

        verify(deliveryRepository, times(1)).findById(TEST_ID_ONE);
        verify(deliveryMapper, times(1)).toDeliveryRecord(TEST_DELIVERY);
    }

    @Test
    void getDeliveryByIdNotFound() {
        when(deliveryRepository.findById(TEST_ID_ONE)).thenReturn(Optional.empty());

        try {
            deliveryService.getDeliveryById(TEST_ID_ONE);
        } catch (DeliveryNotFoundException e) {
        }

        verify(deliveryRepository, times(1)).findById(TEST_ID_ONE);
        verify(deliveryMapper, times(0)).toDeliveryRecord(any());
    }

    @Test
    void updateDeliveryStatusById() {
        when(deliveryRepository.findById(TEST_ID_ONE)).thenReturn(Optional.of(TEST_DELIVERY));

        deliveryService.updateDeliveryStatusById(TEST_ID_ONE, DeliveryStatus.DELIVERED);

        verify(deliveryRepository, times(1)).findById(TEST_ID_ONE);
        verify(deliveryRepository, times(1)).save(any());
        verify(kafkaProducerService, times(1)).sendOrderStatusUpdatedMessage(any());
    }

    @Test
    void updateDeliveryStatusByIdNotFound() {
        when(deliveryRepository.findById(TEST_ID_ONE)).thenReturn(Optional.empty());

        try {
            deliveryService.updateDeliveryStatusById(TEST_ID_ONE, DeliveryStatus.DELIVERED);
        } catch (DeliveryNotFoundException e) {
        }

        verify(deliveryRepository, times(1)).findById(TEST_ID_ONE);
        verify(kafkaProducerService, times(0)).sendOrderStatusUpdatedMessage(any());
    }
}

