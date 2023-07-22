package com.prgrms.gccoffee.api;

import com.prgrms.gccoffee.common.model.Category;
import com.prgrms.gccoffee.common.model.Product;
import com.prgrms.gccoffee.common.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Products API")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/products")
    @Operation(summary = "Get all products")
    public List<Product> products(@RequestParam Optional<Category> category) {
        return category.map(productService::getProductsByCategory)
                .orElseGet(productService::getAllProducts);
    }
}