package com.avenga.productservice.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import productservice.ProductServiceGrpc;

import java.io.IOException;

public class GrpcServer {

    private Server server;

    public GrpcServer(int port, ProductServiceGrpc.ProductServiceImplBase serviceImpl) {
        this.server = ServerBuilder.forPort(port)
                .addService(serviceImpl)
                .build();
    }

    public void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(GrpcServer.this::stop));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
