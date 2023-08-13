package com.avenga.orderservice.exception;

public class NotAvailableProductException extends RuntimeException {
    public NotAvailableProductException(String message) {
        super(message);
    }
}