package com.prgrms.gccoffee.common.service;

import com.prgrms.gccoffee.common.model.Email;
import com.prgrms.gccoffee.common.model.Order;
import com.prgrms.gccoffee.common.model.OrderItem;
import com.prgrms.gccoffee.common.model.OrderStatus;
import com.prgrms.gccoffee.common.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems) {
        var order = new Order(UUID.randomUUID(),
                email,
                address,
                postcode,
                orderItems,
                OrderStatus.ACCEPTED,
                LocalDateTime.now(),
                LocalDateTime.now());

        return orderRepository.insert(order);
    }
}