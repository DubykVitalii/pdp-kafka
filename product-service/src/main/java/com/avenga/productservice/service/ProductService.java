package com.avenga.productservice.service;

import com.avenga.productservice.exception.ProductNotFoundException;
import com.avenga.productservice.mapper.ProductMapper;
import com.avenga.productservice.kafka.ProductKafkaProducer;
import com.avenga.productservice.model.record.NewProductRecord;
import com.avenga.productservice.model.record.ProductRecord;
import com.avenga.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductKafkaProducer productKafkaProducer;

    @Transactional
    public void addProduct(NewProductRecord newProductRecord){
        var productKafkaMessageRecord = productMapper.productToProductKafkaMessageRecord(productRepository.save(productMapper.newProductRecordToProduct(newProductRecord)));
        productKafkaProducer.sendProductCreatedMessage(productKafkaMessageRecord);
    }

    @Transactional
    public void updateProduct(Long id, ProductRecord productRecord){
        var product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        productRepository.save(productMapper.productRecordToUpdateProduct(product, productRecord));
    }

    public ProductRecord getProductById(Long id){
        var product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        return productMapper.productToProductRecord(product);
    }

    public List<ProductRecord> getAllProducts(boolean available) {
        return productRepository.findAllAvailableProducts(available).stream().map(productMapper::productToProductRecord).collect(Collectors.toList());
    }
}
