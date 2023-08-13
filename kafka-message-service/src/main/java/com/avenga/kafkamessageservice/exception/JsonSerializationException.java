package com.avenga.kafkamessageservice.exception;

public class JsonSerializationException extends RuntimeException {
    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
