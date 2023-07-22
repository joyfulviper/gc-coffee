package com.prgrms.gccoffee.api;

import com.prgrms.gccoffee.api.ProductRestController;
import com.prgrms.gccoffee.common.model.Category;
import com.prgrms.gccoffee.common.model.Product;
import com.prgrms.gccoffee.common.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void products() throws Exception {
        var sampleProduct = new Product(UUID.randomUUID(),
                "test-product",
                Category.COFFEE_BEAN_PACKAGE,
                1000L);
        var sampleProducts = Collections.singletonList(sampleProduct);

        when(productService.getAllProducts()).thenReturn(sampleProducts);

        mockMvc.perform(get("/api/v1/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("test-product"))
                .andExpect(jsonPath("$[0].category").value("COFFEE_BEAN_PACKAGE"))
                .andExpect(jsonPath("$[0].price").value(1000L));
    }
}