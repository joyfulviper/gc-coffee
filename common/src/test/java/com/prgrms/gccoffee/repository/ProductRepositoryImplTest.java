package com.prgrms.gccoffee.repository;

import com.prgrms.gccoffee.common.model.Category;
import com.prgrms.gccoffee.common.model.Product;
import com.prgrms.gccoffee.common.repository.ProductRepositoryImpl;
import com.prgrms.gccoffee.common.service.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestConfig.class)
class ProductRepositoryImplTest {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("delete from products", new EmptySqlParameterSource());
    }

    @Test
    @DisplayName("상품을 추가할 수 있다.")
    void insert() {
        //given
        var product = new Product(
                UUID.randomUUID(),
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L,
                "test-description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        //when
        var result = new ProductRepositoryImpl(jdbcTemplate).insert(product);

        //then
        assertNotNull(result);
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void findAll() {
        //given
        var productRepository = new ProductRepositoryImpl(jdbcTemplate);
        var product = new Product(
                UUID.randomUUID(),
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L,
                "test-description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        productRepository.insert(product);

        //when
        var products = productRepository.findAll();

        //then
        assertFalse(products.isEmpty());

    }

    @Test
    @DisplayName("상품을 ID로 조회할 수 있다.")
    void findById() {
        //given
        var id = UUID.randomUUID();
        var expected = new Product(
                id,
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L,
                "test-description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        var productRepository = new ProductRepositoryImpl(jdbcTemplate);

        //when
        productRepository.insert(expected);
        var product = productRepository.findById(id);

        //then
        assertEquals(expected.getProductId(), product.get().getProductId());
    }

    @Test
    @DisplayName("모든 상품을 삭제한다.")
    void deleteAll() {
        //given
        var id = UUID.randomUUID();
        var expected = new Product(
                id,
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L,
                "test-description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        var productRepository = new ProductRepositoryImpl(jdbcTemplate);

        //when
        productRepository.insert(expected);
        productRepository.deleteAll();
        var product = productRepository.findById(id);

        //then
        assertTrue(product.isEmpty());
    }

    @Test
    @DisplayName("상품을 업데이트한다.")
    void update() {
        //given
        var id = UUID.randomUUID();
        var expected = new Product(
                id,
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L,
                "test-description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        var productRepository = new ProductRepositoryImpl(jdbcTemplate);

        //when
        productRepository.insert(expected);
        var updateProduct = productRepository.findById(id).get();
        updateProduct.setProductName("update-product");
        productRepository.update(updateProduct);

        //then
        var product = productRepository.findById(id);
        assertEquals(updateProduct.getProductName(), product.get().getProductName());
    }

    @Test
    @DisplayName("상품을 이름으로 조회한다.")
    void findByName() {
        //given
        var id = UUID.randomUUID();
        var product = new Product(
                id,
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L,
                "test-description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        var productRepository = new ProductRepositoryImpl(jdbcTemplate);
        productRepository.insert(product);

        //when
        var expected = productRepository.findByName(product.getProductName());

        //then
        assertEquals(expected.get().getProductName(), product.getProductName());
    }

    @Test
    @DisplayName("상품을 카테고리로 조회한다.")
    void findByCategory() {
        //given
        var id = UUID.randomUUID();
        var product = new Product(
                id,
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L,
                "test-description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        var productRepository = new ProductRepositoryImpl(jdbcTemplate);
        productRepository.insert(product);

        //when
        var expected = productRepository.findByCategory(product.getCategory());

        //then
        assertFalse(expected.isEmpty());
    }
}