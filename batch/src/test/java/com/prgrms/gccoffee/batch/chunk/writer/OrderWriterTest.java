package com.prgrms.gccoffee.batch.chunk.writer;

import com.prgrms.gccoffee.common.model.Order;
import com.prgrms.gccoffee.common.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.util.UUID;

import static com.prgrms.gccoffee.common.repository.JdbcUtils.toBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@SpringJUnitConfig(OrderWriterTest.TestDataSourceConfiguration.class)
class OrderWriterTest {

    @Configuration
    static class TestDataSourceConfiguration {

        private static final String CREATE_SQL =
                "CREATE TABLE orders (\n" +
                        "    order_id binary(16) Primary Key,\n" +
                        "    email varchar(50) NOT NULL,\n" +
                        "    address varchar(200) NOT NULL,\n" +
                        "    postcode varchar(200) NOT NULL,\n" +
                        "    order_status varchar(50) NOT NULL,\n" +
                        "    created_at datetime(6) NOT NULL,\n" +
                        "    updated_at datetime(6) DEFAULT NULL\n" +
                        ");";

        @Bean
        public DataSource dataSource() {
            EmbeddedDatabaseFactory databaseFactory = new EmbeddedDatabaseFactory();
            databaseFactory.setDatabaseType(H2);
            databaseFactory.setDatabaseName("testdb;MODE=MySQL");
            return databaseFactory.getDatabase();
        }

        @Bean
        public DataSourceInitializer initializer(DataSource dataSource) {
            DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
            dataSourceInitializer.setDataSource(dataSource);

            Resource create = new ByteArrayResource(CREATE_SQL.getBytes());
            dataSourceInitializer.setDatabasePopulator(new ResourceDatabasePopulator(create));

            return dataSourceInitializer;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public OrderWriter orderWriter(DataSource dataSource) {
            return new OrderWriter(dataSource);
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderWriter orderWriter;

    @Test
    void orderItemWriter() {
        // given
        UUID orderId = UUID.randomUUID();
        String email = "test12345@gmail.com";
        String address = "서울시 강남구";
        String postcode = "12345";
        String initialOrderStatus = "ACCEPTED";
        String updatedAt = "2021-08-01 10:00:00";

        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at) VALUES (?, ?, ?, ?, ?, ?)",
                toBytes(orderId), email, address, postcode, initialOrderStatus, updatedAt);

        // when
        orderWriter.orderItemWriter();

        // then
        String updatedOrderStatus = jdbcTemplate.queryForObject("SELECT order_status FROM orders WHERE order_id = ?", String.class, orderId);
        assertThat(updatedOrderStatus).isEqualTo(OrderStatus.ACCEPTED.name());
    }

}