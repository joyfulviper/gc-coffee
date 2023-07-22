package com.prgrms.gccoffee.api;

import com.prgrms.gccoffee.common.model.Email;
import com.prgrms.gccoffee.common.model.Order;
import com.prgrms.gccoffee.common.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Orders API")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/orders")
    @Operation(summary = "Create a new order")
    public Order createOrder(@RequestBody CreateOrderRequest orderRequest) {
        return orderService.createOrder(new Email(orderRequest.email()),
                orderRequest.address(),
                orderRequest.postcode(),
                orderRequest.orderItems());
    }
}