package com.avenga.productservice.config;

import com.avenga.productservice.grpc.GrpcServer;
import com.avenga.productservice.repository.ProductRepository;
import com.avenga.productservice.service.grpc.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import productservice.ProductServiceGrpc;

@Configuration
@RequiredArgsConstructor
public class GrpcConfiguration {

    @Value("${run.in.test}")
    private boolean runInTestEnvironment;

    @Value("${grpc.server.port}")
    private int serverPort;

    @Bean
    public ApplicationRunner grpcServerRunner(ProductServiceGrpc.ProductServiceImplBase productServiceImpl) {
        return args -> {
            GrpcServer server = new GrpcServer(serverPort, productServiceImpl);
            server.start();
            if (!runInTestEnvironment) {
                server.blockUntilShutdown();
            }
        };
    }

    @Bean
    public ProductServiceGrpc.ProductServiceImplBase productServiceImpl(ProductRepository productRepository) {
        return new ProductServiceImpl(productRepository);
    }
}