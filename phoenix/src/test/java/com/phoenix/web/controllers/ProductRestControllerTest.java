package com.phoenix.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.data.dto.ProductDto;
import com.phoenix.data.models.Product;
import com.phoenix.data.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/db/insert.sql"})
class ProductRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Get Product API test")
    void getProductsTest() throws Exception {
        mockMvc.perform(get("/api/product")
                .contentType("application/json"))
                .andExpect(status().is(200))
                .andDo(print());
    }

    @Test
    @DisplayName("Create Product API test")
    void createProductsTest() throws Exception {
//        ProductDto product = new ProductDto();
//        product.setName("Emma Frontend");
//        product.setDescription("Sleeping frontend");
//        product.setPrice(79);
//        product.setQuantity(4);

//        String requestBody = objectMapper.writeValueAsString(product);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/product")
                                                                .param("name", "Jerry")
                                                                .param("description", "Island")
                                                                .param("price", "2455")
                                                                .param("quantity", "3");

        mockMvc.perform(request
                        .contentType("multipart/form-data"))
                .andExpect(status().is(200))
                .andDo(print());
    }

    @Test
    @DisplayName("Update Product API test")
    void updateProductsTest() throws Exception {
        Product product = productRepository.findById(14L).orElse(null);
        assertThat(product).isNotNull();

        mockMvc.perform(patch("/api/product/14")
                        .contentType("application/json-patch+json").
                content(Files.readAllBytes(Path.of("payload.json"))))
                .andExpect(status().is(200))
                .andDo(print());

        product = productRepository.findById(14L).orElse(null);
        assertThat(product).isNotNull();
        assertThat(product.getDescription()).isEqualTo("Covid Shots");
    }

}