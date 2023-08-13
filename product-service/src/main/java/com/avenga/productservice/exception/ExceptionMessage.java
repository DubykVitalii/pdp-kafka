package com.avenga.productservice.exception;

public class ExceptionMessage {
    public static final String PRODUCT_WITH_ID_NOT_FOUND = "There is no product with this identifier. Provided identifier: %s.";
    private ExceptionMessage() {
        throw new IllegalStateException("Utility class");
    }
}
