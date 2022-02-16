package com.phoenix.data.repository;

import com.phoenix.data.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"/db/insert.sql"})
class ProductRepositoryTest {


    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Save a new product to the database")
    void saveProductToDatabaseTest() {
        //create new product
        Product product = new Product();
        product.setName("Bamboo chair");
        product.setDescription("World class bamboo");
        product.setPrice(5540);
        product.setQuantity(9);
        assertThat(product.getId()).isNotNull();

        //save product to database
        productRepository.save(product);
        log.info("Product saved :: {}", product);
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo("Bamboo chair");
        assertThat(product.getPrice()).isEqualTo(5540);
        assertThat(product.getDateCreated()).isNotNull();
    }

    @Test
    @DisplayName("Find an existing product from database")
    void findExistingProductFromDatabaseTest() {
        Product product = productRepository.findById(12L).orElse(null);
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(12);
        assertThat(product.getName()).isEqualTo("Luxury Mop");
        assertThat(product.getPrice()).isEqualTo(2340);
        assertThat(product.getQuantity()).isEqualTo(3);

        log.info("Product retrieved :: {}", product);
    }

    @Test
    @DisplayName("Find all product in the database")
    void findAllProductsTest() {
        List<Product> productList = productRepository.findAll();
        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Find product by name")
    void findProductByNameTest() {
            Product product = productRepository.findByName("Demola").orElse(null);
            assertThat(product).isNotNull();
            assertThat(product.getId()).isEqualTo(13L);
            assertThat(product.getName()).isEqualTo("Demola");
            assertThat(product.getPrice()).isEqualTo(40);
            assertThat(product.getQuantity()).isEqualTo(4);

            log.info("Product retrieved :: {}", product);
    }

    @Test
    @DisplayName("Update a product by id")
    void updateProductByIdTest(){
        Product foundProduct = productRepository.findById(13L).orElse(null);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo("Demola");
        //when
        foundProduct.setPrice(79);
        foundProduct.setName("Emma Frontend");
        //assert
        productRepository.save(foundProduct);
        assertThat(foundProduct.getName()).isEqualTo("Emma Frontend");
        assertThat(foundProduct.getPrice()).isEqualTo(79);
        assertThat(foundProduct.getQuantity()).isEqualTo(4);
        log.info("Product retrieved :: {}", foundProduct);
    }
}