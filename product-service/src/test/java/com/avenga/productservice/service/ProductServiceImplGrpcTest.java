package com.avenga.productservice.service;

import com.avenga.productservice.repository.ProductRepository;
import com.avenga.productservice.service.grpc.ProductServiceImpl;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import productservice.Product;
import productservice.ProductServiceGrpc;

import java.util.Optional;

import static com.avenga.productservice.TestDataConstants.Numbers.TEST_ID_ONE;
import static com.avenga.productservice.TestDataConstants.Products.TEST_PRODUCT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceImplGrpcTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @BeforeEach
    void setUp() throws Exception {
        var serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new ProductServiceImpl(productRepository))
                .build()
                .start());

        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));
    }

    @Test
    void updateProduct_ShouldUpdateProduct() {
        var request = Product.ProductRecord.newBuilder()
                .setId(TEST_ID_ONE)
                .setAvailable(true)
                .setAvailableProductCount(10)
                .build();

        var product = TEST_PRODUCT;
        when(productRepository.findById(TEST_ID_ONE)).thenReturn(Optional.of(product));

        var response = productServiceBlockingStub.updateProduct(request);

        assertEquals(request, response);
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        var request = Product.ProductId.newBuilder()
                .setId(TEST_ID_ONE)
                .build();

        var product = TEST_PRODUCT;
        when(productRepository.findById(TEST_ID_ONE)).thenReturn(Optional.of(product));

        var response = productServiceBlockingStub.getProductById(request);

        assertEquals(product.getId(), response.getId());
    }

    @Test
    void updateProduct_ProductNotFoundException() {
        var request = Product.ProductRecord.newBuilder()
                .setId(TEST_ID_ONE)
                .build();

        when(productRepository.findById(TEST_ID_ONE)).thenReturn(Optional.empty());

        var exception = assertThrows(StatusRuntimeException.class, () -> productServiceBlockingStub.updateProduct(request));
        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
    }

    @Test
    void getProductById_ProductNotFoundException() {
        var request = Product.ProductId.newBuilder()
                .setId(TEST_ID_ONE)
                .build();

        when(productRepository.findById(TEST_ID_ONE)).thenReturn(Optional.empty());

        var exception = assertThrows(StatusRuntimeException.class, () -> productServiceBlockingStub.getProductById(request));
        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
    }

}

