package com.avenga.productservice.controller;

import com.avenga.productservice.model.record.ProductRecord;
import com.avenga.productservice.service.ProductService;
import com.avenga.productservice.model.record.NewProductRecord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public ProductRecord getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductRecord> getAllProduct(@RequestParam("available") boolean available ) {
        return productService.getAllProducts(available);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody @Valid NewProductRecord productRecord) {
        productService.addProduct(productRecord);
    }

    @PutMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable("id") Long id, @RequestBody @Valid ProductRecord productRecord) {
        productService.updateProduct(id, productRecord);
    }
}
