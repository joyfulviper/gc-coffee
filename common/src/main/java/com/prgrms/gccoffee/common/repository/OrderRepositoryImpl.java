package com.prgrms.gccoffee.common.repository;

import com.prgrms.gccoffee.common.model.Order;
import com.prgrms.gccoffee.common.model.OrderItem;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        jdbcTemplate.update(
                "INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                        "VALUES (:orderId, :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order)
        );
        order.getOrderItems().forEach(item ->
                jdbcTemplate.update(
                        "INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                                "VALUES (:orderId, :productId, :category, :price, :quantity, :createdAt, :updatedAt)",
                        toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)
                )
        );
        return order;
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", JdbcUtils.toBytes(order.getOrderId()));
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());

        return paramMap;
    }

    private static Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem orderItem) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", JdbcUtils.toBytes(orderId));
        paramMap.put("productId", JdbcUtils.toBytes(orderItem.productId()));
        paramMap.put("category", orderItem.category().toString());
        paramMap.put("price", orderItem.price());
        paramMap.put("quantity", orderItem.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);

        return paramMap;
    }
}