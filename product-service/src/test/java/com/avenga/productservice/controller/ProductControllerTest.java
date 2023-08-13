package com.avenga.productservice.controller;

import com.avenga.productservice.config.KafkaTestConfig;
import com.avenga.productservice.config.TestContainersConfig;
import com.avenga.productservice.exception.ExceptionMessage;
import com.avenga.productservice.model.persistence.Product;
import com.avenga.productservice.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;

import static com.avenga.productservice.TestDataConstants.Numbers.TEST_NEW_PRICE;
import static com.avenga.productservice.TestDataConstants.Products.TEST_NEW_PRODUCT_RECORD;
import static com.avenga.productservice.TestDataConstants.Products.TEST_PRODUCT;
import static com.avenga.productservice.TestDataConstants.Strings.TEST_STRING_NEW_DESCRIPTION;
import static com.avenga.productservice.TestDataConstants.Strings.TEST_STRING_NEW_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@Import({TestContainersConfig.class, KafkaTestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    private Product product;

    private static Stream<String> createProductWithMissingFieldsSource() {
        String jsonTemplate = """
                {
                    "name": "%s",
                    "description": "%s",
                    "price": %s,
                    "available": %s,
                    "availableProductCount": %s
                }
                """;
        return Stream.of(
                jsonTemplate.formatted("name", "description", null, "true", "10"),
                jsonTemplate.formatted("name", "", "100", "true", "10"),
                jsonTemplate.formatted("", "description", "100", "true", "10"),
                jsonTemplate.formatted("name", "description", "100", null, "10"),
                jsonTemplate.formatted("name", "description", "100", "true", null));
    }

    @BeforeAll
    public void setup() {
        product = TEST_PRODUCT;
        productRepository.saveAndFlush(product);
    }

    @AfterAll
    public void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    @Order(1)
    void getProductById() throws Exception {
        mockMvc.perform(get("/api/v1/products/" + product.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.availableProductCount").value(product.getAvailableProductCount()))
                .andExpect(jsonPath("$.available").value(product.isAvailable()));
    }

    @Test
    @Order(2)
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products").param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].description").value(product.getDescription()));
    }

    @Test
    @Order(3)
    void createProduct() throws Exception {
        var newProductRecord = TEST_NEW_PRODUCT_RECORD;
        var json = """
                {
                  "name": "%s",
                  "description": "%s",
                  "price": %d,
                  "available": %b,
                  "availableProductCount": %d
                }
                """.formatted(newProductRecord.name(),
                newProductRecord.description(),
                newProductRecord.price(),
                newProductRecord.available(),
                newProductRecord.availableProductCount());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    void updateProduct() throws Exception {
        var json = """
                {
                  "id": %d,
                  "name": "%s",
                  "description": "%s",
                  "price": %d,
                  "available": %b,
                  "availableProductCount": %d
                }
                """.formatted(product.getId(), TEST_STRING_NEW_NAME, TEST_STRING_NEW_DESCRIPTION, TEST_NEW_PRICE, product.isAvailable(), product.getAvailableProductCount());

        mockMvc.perform(put("/api/v1/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/products/" + product.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_STRING_NEW_NAME))
                .andExpect(jsonPath("$.description").value(TEST_STRING_NEW_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(TEST_NEW_PRICE));
    }

    @Test
    @Order(5)
    void getProductById_NotFound() throws Exception {
        var nonExistentId = 9999L;
        mockMvc.perform(get("/api/v1/products/" + nonExistentId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ExceptionMessage.PRODUCT_WITH_ID_NOT_FOUND.formatted(nonExistentId)));
    }

    @Test
    @Order(6)
    void updateNonExistentProduct() throws Exception {
        var json = """
                {
                  "id": %d,
                  "name": "name",
                  "description": "description",
                  "price": 100,
                  "available": true,
                  "availableProductCount": 10
                }
                """.formatted(Long.MAX_VALUE);

        mockMvc.perform(put("/api/v1/products/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("createProductWithMissingFieldsSource")
    @Order(7)
    void createProductWithMissingFieldsTest(String json) throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
