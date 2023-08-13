package com.avenga.orderservice.exception;

public class OrderNotFoundException extends ModelNotFoundException{
    public OrderNotFoundException(Long id) {
        super(ExceptionMessage.ORDER_WITH_ID_NOT_FOUND.formatted(id));
    }
}
