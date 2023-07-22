package com.prgrms.gccoffee.common.service;

import com.prgrms.gccoffee.common.model.Category;
import com.prgrms.gccoffee.common.model.Product;
import com.prgrms.gccoffee.common.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product createProduct(String productName, Category category, long price) {
        var product = new Product(UUID.randomUUID(), productName, category, price);
        return productRepository.insert(product);
    }

    @Override
    public Product createProduct(String productName, Category category, long price, String description) {
        var product = new Product(UUID.randomUUID(), productName, category, price, description, LocalDateTime.now(), LocalDateTime.now());
        return productRepository.insert(product);
    }
}