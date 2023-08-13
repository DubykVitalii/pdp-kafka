package com.avenga.orderservice.controller;

import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.record.OrderRecord;
import com.avenga.orderservice.service.OrderService;
import com.avenga.orderservice.model.record.NewOrderRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public OrderRecord getOrderById(@PathVariable("id") Long id){
        return orderService.getOrderById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderRecord> findAllOrdersByStatus(@RequestParam(value = "status", required = false) OrderStatus status){
        return orderService.findAllOrdersByStatus(status);
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderRecord> findAllOrdersByCustomerId(@PathVariable("id") Long id){
        return orderService.findAllOrdersByCustomerId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderRecord createOrder(@RequestBody NewOrderRecord newOrderRecord){
        return orderService.createOrder(newOrderRecord);
    }

    @PutMapping({"/complete/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public void completeOrder(@PathVariable("id") Long id){
        orderService.completeOrder(id);
    }

    @PutMapping({"/cancel/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable("id") Long id){
        orderService.cancelOrder(id);
    }
}
