package com.avenga.orderservice.mapper;

import com.avenga.orderservice.model.persistence.order.OrderItem;
import com.avenga.orderservice.model.record.OrderItemRecord;
import com.avenga.orderservice.model.record.ProductDetailsRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderItemMapper {
    @Mapping(target = "productDetails", source = ".")
    OrderItemRecord orderItemToOrderItemRecord(OrderItem orderItem);

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "name", source = "productName")
    @Mapping(target = "price", source = "productPrice")
    ProductDetailsRecord toProductDetailsRecord(OrderItem orderItem);
}
