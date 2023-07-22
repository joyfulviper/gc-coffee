package com.prgrms.gccoffee.common.service;

import com.prgrms.gccoffee.common.model.Category;
import com.prgrms.gccoffee.common.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(Category category);

    Product createProduct(String productName, Category category, long price);

    Product createProduct(String productName, Category category, long price, String description);
}