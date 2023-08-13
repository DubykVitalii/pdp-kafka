package com.avenga.orderservice.aspects;

import com.avenga.orderservice.exception.OrderNotFoundException;
import com.avenga.orderservice.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@Slf4j
@RestControllerAdvice
public class ErrorRestControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        var message = new StringBuilder();

        for (FieldError fieldError : fieldErrors) {
            if (fieldError != null)
                message.append("Field %s: %s \n".formatted(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        log.error(ex.getMessage(), ex);
        return new ErrorResponse(message.toString(), HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorResponse handleModelNotFoundException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ErrorResponse handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorResponse(ex.getMessage(), HttpStatus.REQUEST_TIMEOUT.value());
    }
}

