//package com.avenga.productservice;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.avenga.productservice.model.persistence.Product;
//import com.avenga.productservice.model.record.NewProductRecord;
//import com.avenga.productservice.model.record.ProductKafkaMessageRecord;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.restassured.http.ContentType;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import java.util.*;
//
//@RunWith(SpringRunner.class)
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
//@TestPropertySource(properties = {
//        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
//})
//public class ProductE2ETest {
//
//    private static final String GROUP_ID = "kafka-service-group";
//    private static final String CREATE_PRODUCT_TOPIC = "products";
//    private int countNewMessages = 0;
//
//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafkaBroker;
//
//    private final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    public void testCreateProductE2EFlow() throws Exception {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        props.put("group.id", GROUP_ID + "-" + UUID.randomUUID().toString());
//        props.put("key.deserializer", StringDeserializer.class.getName());
//        props.put("value.deserializer", StringDeserializer.class.getName());
//        props.put("auto.offset.reset", "earliest");
//
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Collections.singletonList(CREATE_PRODUCT_TOPIC));
//        List<ProductKafkaMessageRecord> allMessages = new ArrayList<>();
//
//        // Create new product
//        NewProductRecord newProductRecord = new NewProductRecord("Product Name", "Product Description", 100, true, 10);
//        given() .baseUri("http://localhost:6001")
//                .contentType(ContentType.JSON)
//                .body(newProductRecord)
//                .when()
//                .post("/api/v1/products")
//                .then()
//                .statusCode(HttpStatus.CREATED.value());
//
//        // Verify that product was saved in the database
////        Product savedProduct = getProductFromDatabase();
////        assertNotNull(savedProduct);
////        assertEquals("Product Name", savedProduct.getName());
//
//        // String message = getSingleRecord(consumer, "products").value();
////        ProductKafkaMessageRecord productKafkaMessageRecord = objectMapper.readValue(message, ProductKafkaMessageRecord.class);
////        assertEquals(savedProduct.getId(), productKafkaMessageRecord.id());
////        assertEquals("Product Name", productKafkaMessageRecord.name());
//
//        postgreSQLContainer.stop();
//    }
//
//    @KafkaListener(topics = CREATE_PRODUCT_TOPIC, groupId = GROUP_ID)
//    public void listenCreateProduct(ConsumerRecord<String, String> record) {
//        String recordValueAsJson = record.value();
//        var count = countNewMessages + 1;
//        System.out.println(count);
//    }
//
////    // Helper method to query the product from the database
////    private Product getProductFromDatabase() {
////        // Implement logic to query the product from the PostgreSQL container
////        // ...
////    }
//}
//
//
