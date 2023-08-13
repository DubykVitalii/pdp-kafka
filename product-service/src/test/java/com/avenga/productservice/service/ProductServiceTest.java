package com.avenga.productservice.service;

import com.avenga.productservice.exception.ProductNotFoundException;
import com.avenga.productservice.kafka.ProductKafkaProducer;
import com.avenga.productservice.mapper.ProductMapper;
import com.avenga.productservice.model.persistence.Product;
import com.avenga.productservice.model.record.ProductRecord;
import com.avenga.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static com.avenga.productservice.TestDataConstants.Products.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductKafkaProducer productKafkaProducer;

    private ProductRecord productRecord;
    private Product product;

    @BeforeEach
    void setUp() {
        productRecord = TEST_PRODUCT_RECORD;
        product = TEST_PRODUCT;
    }

    @Test
    void addProduct_ShouldCreateAndSendMessage() {
        var newProductRecord = TEST_NEW_PRODUCT_RECORD;
        when(productMapper.newProductRecordToProduct(any())).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);

        productService.addProduct(newProductRecord);

        verify(productRepository, times(1)).save(product);
        verify(productKafkaProducer, times(1)).sendProductCreatedMessage(any());
    }

    @Test
    void updateProduct_ShouldUpdateProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        productService.updateProduct(1L, productRecord);

        verify(productRepository, times(1)).save(any());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.productToProductRecord(product)).thenReturn(productRecord);

        var result = productService.getProductById(1L);

        assertEquals(productRecord, result);
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void getAllProducts_ShouldReturnProducts() {
        when(productRepository.findAllAvailableProducts(anyBoolean())).thenReturn(Collections.singletonList(product));
        when(productMapper.productToProductRecord(product)).thenReturn(productRecord);

        var result = productService.getAllProducts(true);

        assertEquals(1, result.size());
        assertEquals(productRecord, result.get(0));
    }
}


