package com.prgrms.gccoffee.controller;

import com.prgrms.gccoffee.common.model.Category;
import com.prgrms.gccoffee.common.model.Product;
import com.prgrms.gccoffee.common.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void products() throws Exception {
        // Given
        List<Product> mockProducts = List.of();

        // When
        when(productService.getAllProducts()).thenReturn(mockProducts);

        // then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", mockProducts))
                .andExpect(view().name("product-list"));
    }

    @Test
    @DisplayName("상품 추가 페이지를 조회한다.")
    void testNewProduct() throws Exception {
        mockMvc.perform(get("/new-product"))
                .andExpect(status().isOk())
                .andExpect(view().name("new-product"));
    }

    @Test
    @DisplayName("상품을 추가한다.")
    void newProduct() throws Exception {
        // Given: Sample CreateProductRequest data
        String productName = "testName";
        Category category = Category.COFFEE_BEAN_PACKAGE;
        long price = 10L;
        String description = "testDescription";

        // Perform the MockMvc request and check the expected result
        mockMvc.perform(post("/products")
                        .param("productName", productName)
                        .param("category", category.name())
                        .param("price", String.valueOf(price))
                        .param("description", description))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService, times(1)).createProduct(productName, category, price, description);
    }
}