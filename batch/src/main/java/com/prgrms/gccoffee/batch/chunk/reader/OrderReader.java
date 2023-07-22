package com.prgrms.gccoffee.batch.chunk.reader;

import com.prgrms.gccoffee.common.model.Email;
import com.prgrms.gccoffee.common.model.Order;
import com.prgrms.gccoffee.common.model.OrderStatus;
import com.prgrms.gccoffee.common.repository.JdbcUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class OrderReader {

    private final DataSource dataSource;

    @Autowired
    public OrderReader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @StepScope
    @Bean
    SynchronizedItemStreamReader<Order> orderItemReader() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM orders");
        queryProvider.setWhereClause("WHERE order_status = 'ACCEPTED'");
        queryProvider.setSortKey("created_at");

        JdbcPagingItemReader<Order> reader = new JdbcPagingItemReaderBuilder<Order>()
                .dataSource(dataSource)
                .name("orderReader")
                .fetchSize(20)
                .queryProvider(Objects.requireNonNull(queryProvider.getObject()))
                .rowMapper((rs, rowNum) -> {
                    var orderId = JdbcUtils.toUUID(rs.getBytes("order_id"));
                    var email = new Email(rs.getString("email"));
                    var createdAt = JdbcUtils.toLocalDateTime(rs.getTimestamp("created_at"));
                    var order = new Order(orderId, email, createdAt);
                    order.setOrderStatus(OrderStatus.READY_FOR_DELIVERY);
                    return order;
                })
                .build();

        reader.afterPropertiesSet();

        return new SynchronizedItemStreamReaderBuilder<Order>()
                .delegate(reader)
                .build();
    }
}