package com.avenga.productservice;

import com.avenga.productservice.model.persistence.Product;
import com.avenga.productservice.model.record.NewProductRecord;
import com.avenga.productservice.model.record.ProductKafkaMessageRecord;
import com.avenga.productservice.model.record.ProductRecord;

import static com.avenga.productservice.TestDataConstants.Numbers.TEST_ID_ONE;
import static com.avenga.productservice.TestDataConstants.Strings.*;

public class TestDataConstants {

    public static class Numbers {
        public static final Long TEST_ID_ONE = 1L;
        public static final Long TEST_ID_TWO = 2L;
        public static final Integer TEST_NEW_PRICE = 999;
    }

    public static class Strings {
        public static final String TEST_STRING_NAME_ONE = "Test name 1";
        public static final String TEST_STRING_NAME_TWO = "Test name 2";
        public static final String TEST_STRING_DESCRIPTION_ONE = "Test description 1";
        public static final String TEST_STRING_DESCRIPTION_TWO = "Test description 2";
        public static final String TEST_STRING_NEW_NAME = "New  name";
        public static final String TEST_STRING_NEW_DESCRIPTION = "New Description";
    }

    public static class Products {
        public static final Product TEST_PRODUCT = Product.builder().id(TEST_ID_ONE).name(TEST_STRING_NAME_ONE).available(true).availableProductCount(200).description(TEST_STRING_DESCRIPTION_ONE).price(100).build();
        public static final ProductRecord TEST_PRODUCT_RECORD = new ProductRecord(TEST_ID_ONE, TEST_STRING_NAME_ONE, TEST_STRING_DESCRIPTION_ONE, 100, true, 200);
        public static final NewProductRecord TEST_NEW_PRODUCT_RECORD = new NewProductRecord(TEST_STRING_NAME_ONE, TEST_STRING_DESCRIPTION_ONE, 100, true, 200);
        public static final ProductKafkaMessageRecord TEST_PRODUCT_KAFKA_MESSAGE = new ProductKafkaMessageRecord(TEST_ID_ONE, TEST_STRING_NAME_ONE, TEST_STRING_DESCRIPTION_ONE, 100);
    }
}

