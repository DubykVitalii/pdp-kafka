package com.avenga.productservice.exception;

public class ProductNotFoundException extends ModelNotFoundException {
    public ProductNotFoundException(Long id) {
        super(ExceptionMessage.PRODUCT_WITH_ID_NOT_FOUND.formatted(id));
    }
}
