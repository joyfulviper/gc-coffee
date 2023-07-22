package com.prgrms.gccoffee.batch.chunk.reader;

import com.prgrms.gccoffee.batch.job.DeliveryStart;
import com.prgrms.gccoffee.common.model.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.prgrms.gccoffee.common.repository.JdbcUtils.toBytes;
import static com.prgrms.gccoffee.common.repository.JdbcUtils.toUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@SpringJUnitConfig(OrderReaderTest.TestDataSourceConfiguration.class)
class OrderReaderTest {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private OrderReader job;

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
        public OrderReader orderReader(DataSource dataSource) {
            return new OrderReader(dataSource);
        }
    }

    @Test
    @DisplayName("주문 상태가 ACCEPTED인 주문만 조회한다.")
    void orderReader() throws Exception {

        // given
        var order_id = UUID.randomUUID();
        var email = "test12345@gmail.com";
        var address = "서울시 강남구";
        var postcode = "12345";
        var order_status = "ACCEPTED";
        var created_at = "2021-08-01 10:00:00";

        // when
        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at) VALUES (?, ?, ?, ?, ?, ?)",
                toBytes(order_id), email, address, postcode, order_status, created_at);

        SynchronizedItemStreamReader<Order> reader = job.orderItemReader();
        reader.afterPropertiesSet();


        // then
        assertThat(reader.read().getEmail().getAddress()).isEqualTo(email);
    }

    @Test
    @DisplayName("멀티 스레드에서 중복 없이 아이템을 읽는다.")
    void multiThreadedReadingTest() throws Exception {

        //given
        int numRecords = 10;
        for (int i = 0; i < numRecords; i++) {
            var order_id = UUID.randomUUID();
            var email = "test" + i + "@gmail.com";
            var address = "서울시 강남구";
            var postcode = "12345";
            var order_status = "ACCEPTED";
            var created_at = "2021-08-01 10:00:00";
            jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at) VALUES (?, ?, ?, ?, ?, ?)",
                    toBytes(order_id), email, address, postcode, order_status, created_at);
        }

        SynchronizedItemStreamReader<Order> reader = job.orderItemReader();
        reader.afterPropertiesSet();

        Set<UUID> readEmails = Collections.synchronizedSet(new HashSet<>()); // 동시에 접근할 수 있는 HashSet
        ExecutorService executor = Executors.newFixedThreadPool(numRecords);

        // when
        for (int i = 0; i < numRecords; i++) {
            executor.submit(() -> {
                try {
                    Order order = reader.read();
                    if (order != null) {
                        readEmails.add(order.getOrderId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES); // 최대 1분 동안 대기

        // then
        assertThat(readEmails.size()).isEqualTo(numRecords); // 중복이 없다면 읽은 이메일의 수는 numRecords와 동일해야 합니다.
    }
}