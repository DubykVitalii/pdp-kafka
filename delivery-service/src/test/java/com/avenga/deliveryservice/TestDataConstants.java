package com.avenga.deliveryservice;

import com.avenga.deliveryservice.model.enumeration.DeliveryStatus;
import com.avenga.deliveryservice.model.enumeration.OrderStatus;
import com.avenga.deliveryservice.model.persistance.Delivery;
import com.avenga.deliveryservice.model.persistance.Order;
import com.avenga.deliveryservice.model.persistance.OrderItem;
import com.avenga.deliveryservice.model.persistance.ProductDetails;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;

import static com.avenga.deliveryservice.TestDataConstants.Numbers.*;
import static com.avenga.deliveryservice.TestDataConstants.Orders.TEST_ORDER;

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
    }

    public static class Orders {
        public static final ProductDetails TEST_PRODUCT_DETAILS = ProductDetails.of().id(TEST_ID_ONE).name(Strings.TEST_PRODUCT_NAME).price(TEST_PRICE).build();
        public static final OrderItem TEST_ORDER_ITEM = OrderItem.of().productDetails(TEST_PRODUCT_DETAILS).quantity(TEST_QUANTITY).id(TEST_ID_ONE).build();
        public static final Order TEST_ORDER = Order.of().id(TEST_ID_ONE).deliveryAddress(Strings.TEST_DELIVERY_ADDRESS).customerId(TEST_ID_ONE).status(OrderStatus.IN_PROGRESS).orderItems(List.of(TEST_ORDER_ITEM)).createdAt(LocalDate.now()).build();

    }

    public static class Deliveries {
        public static final Delivery TEST_DELIVERY = Delivery.of().id(TEST_ID_ONE).status(DeliveryStatus.PENDING).order(TEST_ORDER).build();
    }
}

