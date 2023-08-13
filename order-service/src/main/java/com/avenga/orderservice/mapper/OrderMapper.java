package com.avenga.orderservice.mapper;

import com.avenga.orderservice.model.persistence.order.Order;
import com.avenga.orderservice.model.persistence.order.OrderItem;
import com.avenga.orderservice.model.record.NewOrderRecord;
import com.avenga.orderservice.model.record.OrderItemRecord;
import com.avenga.orderservice.model.record.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = OrderItemMapper.class, imports = {LocalDate.class, ZoneOffset.class})
public interface OrderMapper {

    @Mapping(target = "createdAt", expression = "java(LocalDate.now(ZoneOffset.UTC))")
    @Mapping(target = "status", constant = "IN_PROGRESS")
    @Mapping(target = "orderItems", ignore = true)
    Order newOrderRecordToOrder(NewOrderRecord newOrderRecord);


    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "orderItemsToOrderItemRecords")
    OrderRecord orderToOrderRecord(Order order);

    @Named("orderItemsToOrderItemRecords")
    default List<OrderItemRecord> orderItemsToOrderItemRecords(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::orderItemToOrderItemRecord)
                .collect(Collectors.toList());
    }

    @Named("orderItemToOrderItemRecord")
    default OrderItemRecord orderItemToOrderItemRecord(OrderItem orderItem) {
        return Mappers.getMapper(OrderItemMapper.class).orderItemToOrderItemRecord(orderItem);
    }
}
