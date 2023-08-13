package com.avenga.orderservice.service;

import com.avenga.orderservice.model.record.OrderRecord;
import com.avenga.orderservice.client.ProductServiceGrpcClient;
import com.avenga.orderservice.exception.NotAvailableProductException;
import com.avenga.orderservice.exception.NotEnoughProductException;
import com.avenga.orderservice.exception.OrderNotFoundException;
import com.avenga.orderservice.kafka.OrderKafkaProducer;
import com.avenga.orderservice.mapper.OrderMapper;
import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.persistence.order.Order;
import com.avenga.orderservice.model.persistence.order.OrderItem;
import com.avenga.orderservice.model.record.NewOrderItemRecord;
import com.avenga.orderservice.model.record.NewOrderRecord;
import com.avenga.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import productservice.Product;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceGrpcClient productServiceGrpcClient;
    private final OrderMapper orderMapper;
    private final OrderKafkaProducer orderKafkaProducer;

    public OrderRecord getOrderById(Long id) {
        var order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        return orderMapper.orderToOrderRecord(order);
    }

    public List<OrderRecord> findAllOrdersByStatus(OrderStatus status) {
        return orderRepository.findAllOrdersByStatus(status).stream().map(orderMapper::orderToOrderRecord).collect(Collectors.toList());
    }

    public List<OrderRecord> findAllOrdersByCustomerId(Long customerId) {
        return orderRepository.findAllOrdersByCustomerId(customerId).stream().map(orderMapper::orderToOrderRecord).collect(Collectors.toList());
    }

    @Transactional
    public OrderRecord createOrder(NewOrderRecord newOrderRecord) {
        var order = orderMapper.newOrderRecordToOrder(newOrderRecord);
        order.setOrderItems(createOrderItems(newOrderRecord, order));
        var orderRecord = orderMapper.orderToOrderRecord(orderRepository.save(order));
        orderKafkaProducer.sendOrderCreatedMessage(orderRecord);
        return orderRecord;
    }

    private List<OrderItem> createOrderItems(NewOrderRecord newOrderRecord, Order order) {
        return newOrderRecord.orderItems().stream().map(orderItem -> {
            var product = productServiceGrpcClient.getProductById(orderItem.productId());
            checkProductAvailability(product);
            checkProductQuantity(product, orderItem.quantity());
            updateProduct(product, orderItem);
            return OrderItem.of()
                    .order(order)
                    .productId(product.getId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .quantity(orderItem.quantity()).build();
        }).collect(Collectors.toList());
    }

    private void updateProduct(Product.ProductRecord product, NewOrderItemRecord orderItem) {
        var availableProductCount = product.getAvailableProductCount() - orderItem.quantity();
        var modifiedProductRecord = product.toBuilder()
                .setAvailableProductCount(availableProductCount)
                .setAvailable(availableProductCount > 0)
                .build();
        productServiceGrpcClient.updateProduct(modifiedProductRecord);
    }

    private void checkProductAvailability(Product.ProductRecord product) {
        if (!product.getAvailable()) {
            throw new NotAvailableProductException("Product " + product.getId() + " is not available.");
        }
    }

    private void checkProductQuantity(Product.ProductRecord product, int quantity) {
        if (product.getAvailableProductCount() < quantity) {
            throw new NotEnoughProductException("Product " + product.getId() + " is not available in the requested quantity.");
        }
    }

    @Transactional
    public void completeOrder(Long id) {
        var order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long id) {
        var order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }
}
