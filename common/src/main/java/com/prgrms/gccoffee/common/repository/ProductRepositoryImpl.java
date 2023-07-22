package com.prgrms.gccoffee.common.repository;

import com.prgrms.gccoffee.common.model.Category;
import com.prgrms.gccoffee.common.model.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("select * from products", productRowMapper);
    }

    @Override
    public Product insert(Product product) {
        System.out.println(product.getProductName());
        var update = jdbcTemplate.update(
                "insert into products(product_id, product_name, category, price, description, created_at, updated_at) " +
                        "values (:productId, :productName, :category, :price, :description, :createdAt, :updatedAt)",
                toParamMap(product));

        if (update != 1) {
            throw new RuntimeException("Noting was inserted");
        }

        return product;
    }

    @Override
    public Product update(Product product) {
        var update = jdbcTemplate.update(
                "update products set product_name = :productName, category = :category, price = :price, description = :description, updated_at = :updatedAt where product_id = :productId",
                toParamMap(product)
        );

        if (update != 1) {
            throw new RuntimeException("Nothing was updated");
        }

        return product;
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from products where product_id = :productId",
                            Collections.singletonMap("productId", productId.toString()),
                            productRowMapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String productName) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from products where product_name = :productName",
                            Collections.singletonMap("productName", productName),
                            productRowMapper
                    ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return jdbcTemplate.query(
                "select * from products where category = :category",
                Collections.singletonMap("category", category.toString()),
                productRowMapper
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from products", Collections.emptyMap());
    }

    private static final RowMapper<Product> productRowMapper = (result, i) -> {
        var productId = JdbcUtils.toUUID(result.getBytes("product_id"));
        var productName = result.getString("product_name");
        var category = Category.valueOf(result.getString("category"));
        var price = result.getLong("price");
        var description = result.getString("description");
        var createdAt = JdbcUtils.toLocalDateTime(result.getTimestamp("created_at"));
        var updatedAt = JdbcUtils.toLocalDateTime(result.getTimestamp("updated_at"));

        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    };

    private static Map<String, Object> toParamMap(Product product) {
        return Map.of(
                "productId", JdbcUtils.toBytes(product.getProductId()),
                "productName", product.getProductName(),
                "category", product.getCategory().toString(),
                "price", product.getPrice(),
                "description", product.getDescription(),
                "createdAt", product.getCreatedAt(),
                "updatedAt", product.getUpdatedAt()
        );
    }
}