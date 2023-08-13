package com.avenga.deliveryservice.exception;

import static com.avenga.deliveryservice.exception.ExceptionMessage.DELIVERY_WITH_ID_NOT_FOUND;

public class DeliveryNotFoundException extends RuntimeException{
    public DeliveryNotFoundException(Long id) {
        super(DELIVERY_WITH_ID_NOT_FOUND.formatted(id));
    }
}
