package com.avenga.kafkamessageservice.controller;

import com.avenga.kafkamessageservice.model.OrderStatusKafkaMessageRecord;
import com.avenga.kafkamessageservice.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders/status")
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(@RequestBody OrderStatusKafkaMessageRecord orderStatusKafkaMessageRecord){
        orderStatusService.updateOrderStatus(orderStatusKafkaMessageRecord);
    }
}
