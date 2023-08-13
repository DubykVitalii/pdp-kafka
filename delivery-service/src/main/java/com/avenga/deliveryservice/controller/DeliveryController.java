package com.avenga.deliveryservice.controller;

import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.avenga.deliveryservice.model.record.DeliveryRecord;
import com.avenga.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DeliveryRecord> getAllDeliveries(@RequestParam(value = "status", required = false) DeliveryStatus status) {
        return deliveryService.getAllDelivery(status);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryRecord getDeliveryById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateDeliveryStatusById(@PathVariable Long id, @RequestParam DeliveryStatus status) {
        deliveryService.updateDeliveryStatusById(id, status);
    }
}

