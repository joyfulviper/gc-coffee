package com.prgrms.gccoffee.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.gccoffee.common.model.*;
import com.prgrms.gccoffee.common.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() throws Exception {
        //given
        var orderItem = new OrderItem(
                UUID.randomUUID(),
                Category.COFFEE_BEAN_PACKAGE,
                12000L,
                1
        );

        var createOrderRequest = new CreateOrderRequest(
                "test@email.com",
                "test address",
                "123456",
                List.of(orderItem));

        var order = new Order(
                UUID.randomUUID(),
                new Email(createOrderRequest.email()),
                createOrderRequest.address(),
                createOrderRequest.postcode(),
                List.of(orderItem),
                OrderStatus.ACCEPTED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        //when

        when(orderService.createOrder(any(), any(), any(), any())).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email.address").value(order.getEmail().getAddress()))
                .andExpect(jsonPath("$.address").value(order.getAddress()))
                .andExpect(jsonPath("$.postcode").value(order.getPostcode()))
                .andExpect(jsonPath("$.orderItems[0].productId").value(orderItem.productId().toString()))
                .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus().toString()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

}