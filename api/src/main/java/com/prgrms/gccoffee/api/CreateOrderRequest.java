package com.prgrms.gccoffee.api;

import com.prgrms.gccoffee.common.model.OrderItem;

import java.util.List;

public record CreateOrderRequest(
        String email,
        String address,
        String postcode,
        List<OrderItem> orderItems
) {
}