package com.avenga.orderservice.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import productservice.Product;
import productservice.ProductServiceGrpc;

@Component
public class ProductServiceGrpcClient {

    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @Value("${grpc.product-service.host}")
    private String productServiceHost;

    @Value("${grpc.product-service.port}")
    private int productServicePort;

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(productServiceHost, productServicePort)
                .usePlaintext()
                .build();
        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(managedChannel);
    }

    public Product.ProductRecord getProductById(Long id) {
        Product.ProductId request = Product.ProductId.newBuilder()
                .setId(id)
                .build();
        return productServiceBlockingStub.getProductById(request);
    }

    public Product.ProductRecord updateProduct(Product.ProductRecord product) {
        return productServiceBlockingStub.updateProduct(product);
    }
}