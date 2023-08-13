package com.avenga.orderservice;

import com.avenga.orderservice.model.enumeration.OrderStatus;
import com.avenga.orderservice.model.persistence.order.Order;
import com.avenga.orderservice.model.persistence.order.OrderItem;
import com.avenga.orderservice.model.record.*;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.avenga.orderservice.TestDataConstants.Numbers.*;
import static com.avenga.orderservice.TestDataConstants.Strings.TEST_DELIVERY_ADDRESS;
import static com.avenga.orderservice.TestDataConstants.Strings.TEST_PRODUCT_NAME;

@UtilityClass
public class TestDataConstants {

    public static class Numbers {
        public static final Long TEST_ID_ONE = 1L;
        public static final Integer TEST_QUANTITY = 5;
        public static final Integer TEST_PRICE = 100;
    }

    public static class Strings {
        public static final String TEST_DELIVERY_ADDRESS = "Test address";
        public static final String TEST_PRODUCT_NAME = "Test Product";
        public static final String TEST_PRODUCT_DESCRIPTION = "Test Description";
    }

    public static class OrderRecords {
        public static final OrderItemRecord TEST_ORDER_ITEM_RECORD = new OrderItemRecord(TEST_ID_ONE, new ProductDetailsRecord(TEST_ID_ONE, TEST_PRODUCT_NAME, TEST_PRICE), TEST_QUANTITY);
        public static final List<OrderItemRecord> TEST_ORDER_ITEM_RECORDS = Arrays.asList(TEST_ORDER_ITEM_RECORD);
        public static final OrderRecord TEST_ORDER_RECORD = new OrderRecord(TEST_ID_ONE, LocalDate.now(), TEST_ID_ONE, TEST_DELIVERY_ADDRESS, TEST_ORDER_ITEM_RECORDS, OrderStatus.IN_PROGRESS);
        public static final NewOrderItemRecord TEST_NEW_ORDER_ITEM_RECORD = new NewOrderItemRecord(TEST_ID_ONE, TEST_QUANTITY);
        public static final List<NewOrderItemRecord> TEST_NEW_ORDER_ITEM_RECORDS = Arrays.asList(TEST_NEW_ORDER_ITEM_RECORD);
        public static final NewOrderRecord TEST_NEW_ORDER_RECORD = new NewOrderRecord(TEST_ID_ONE, TEST_DELIVERY_ADDRESS, TEST_NEW_ORDER_ITEM_RECORDS);
    }

    public static class Orders {
        public static final Order TEST_ORDER = Order.of().id(TEST_ID_ONE).deliveryAddress(TEST_DELIVERY_ADDRESS).customerId(TEST_ID_ONE).status(OrderStatus.IN_PROGRESS).createdAt(LocalDate.now()).build();
        public static final OrderItem TEST_ORDER_ITEM = OrderItem.of().productName(TEST_PRODUCT_NAME).order(TEST_ORDER).quantity(TEST_QUANTITY).id(TEST_ID_ONE).productPrice(TEST_PRICE).productId(TEST_ID_ONE).build();
        public static final List<OrderItem> TEST_ORDER_ITEMS = Arrays.asList(TEST_ORDER_ITEM);
    }
}

