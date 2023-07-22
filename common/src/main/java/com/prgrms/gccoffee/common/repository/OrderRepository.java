package com.prgrms.gccoffee.common.repository;

import com.prgrms.gccoffee.common.model.Order;

public interface OrderRepository {

    Order insert(Order order);
}