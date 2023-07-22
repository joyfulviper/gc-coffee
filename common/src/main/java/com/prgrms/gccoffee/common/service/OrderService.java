package com.prgrms.gccoffee.common.service;

import com.prgrms.gccoffee.common.model.Email;
import com.prgrms.gccoffee.common.model.Order;
import com.prgrms.gccoffee.common.model.OrderItem;

import java.util.List;

public interface OrderService {

    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);
}