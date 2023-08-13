package com.avenga.deliveryservice.exception;

public class ExceptionMessage {
    public static final String DELIVERY_WITH_ID_NOT_FOUND = "There is no delivery with this identifier. Provided identifier: %s.";
    private ExceptionMessage() {
        throw new IllegalStateException("Utility class");
    }
}
