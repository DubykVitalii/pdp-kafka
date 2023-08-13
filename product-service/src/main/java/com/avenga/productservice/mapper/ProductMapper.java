package com.avenga.productservice.mapper;

import com.avenga.productservice.model.persistence.Product;
import com.avenga.productservice.model.record.NewProductRecord;
import com.avenga.productservice.model.record.ProductKafkaMessageRecord;
import com.avenga.productservice.model.record.ProductRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProductMapper {

    ProductRecord productToProductRecord(Product product);

    Product newProductRecordToProduct(NewProductRecord newProductRecord);

    @Mapping(target = "id", ignore = true)
    Product productRecordToUpdateProduct(@MappingTarget Product product, ProductRecord productRecord);

    ProductKafkaMessageRecord productToProductKafkaMessageRecord(Product product);
}
