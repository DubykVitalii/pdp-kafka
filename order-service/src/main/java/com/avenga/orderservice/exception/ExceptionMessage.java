package com.avenga.orderservice.exception;

public class ExceptionMessage {
    public static final String ORDER_WITH_ID_NOT_FOUND = "There is no order with this identifier. Provided identifier: %s.";
    private ExceptionMessage() {
        throw new IllegalStateException("Utility class");
    }
}
