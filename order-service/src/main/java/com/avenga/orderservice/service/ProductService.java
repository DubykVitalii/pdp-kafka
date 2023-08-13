package com.avenga.orderservice.service;

import com.avenga.orderservice.model.persistence.product.Product;

public interface ProductService {
    Product getProductById(Long id);
    void updateProduct(Product product);
}
