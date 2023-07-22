package com.prgrms.gccoffee.batch.chunk.writer;

import com.prgrms.gccoffee.common.model.Order;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import javax.sql.DataSource;

import static com.prgrms.gccoffee.common.repository.JdbcUtils.toBytes;

@Configuration
public class OrderWriter {

    private final DataSource dataSource;

    public OrderWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Order> orderItemWriter() {
        return new JdbcBatchItemWriterBuilder<Order>()
                .itemSqlParameterSourceProvider(new CustomOrderSqlParameterSourceProvider())
                .sql("UPDATE orders SET order_status = :orderStatus WHERE order_id = :orderId")
                .dataSource(dataSource)
                .build();
    }

    private static class CustomOrderSqlParameterSourceProvider extends BeanPropertyItemSqlParameterSourceProvider<Order> {

        @Override
        @NonNull
        public SqlParameterSource createSqlParameterSource(Order order) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();

            parameterSource.addValue("orderId", toBytes(order.getOrderId()));
            parameterSource.addValue("orderStatus", order.getOrderStatus().name());

            return parameterSource;
        }
    }
}