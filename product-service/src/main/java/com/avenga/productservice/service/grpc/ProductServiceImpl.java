package com.avenga.productservice.service.grpc;

import com.avenga.productservice.exception.ProductNotFoundException;
import com.avenga.productservice.repository.ProductRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import productservice.Product;
import productservice.ProductServiceGrpc;

@RequiredArgsConstructor
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void updateProduct(Product.ProductRecord request, StreamObserver<Product.ProductRecord> responseObserver) {
        try {
            var product = productRepository.findById(request.getId())
                    .orElseThrow(() -> new ProductNotFoundException(request.getId()));
            product.setAvailable(request.getAvailable());
            product.setAvailableProductCount(request.getAvailableProductCount());
            productRepository.save(product);
            responseObserver.onNext(request);
            responseObserver.onCompleted();
        } catch (ProductNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getProductById(Product.ProductId request, StreamObserver<Product.ProductRecord> responseObserver) {
        try {
            var id = request.getId();
            var product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
            Product.ProductRecord response = Product.ProductRecord.newBuilder()
                    .setId(product.getId())
                    .setName(product.getName())
                    .setPrice(product.getPrice())
                    .setDescription(product.getDescription())
                    .setAvailableProductCount(product.getAvailableProductCount())
                    .setAvailable(product.isAvailable())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (ProductNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }

}
