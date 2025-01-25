package com.klymenko.newmarketapi.controller;

import com.klymenko.newmarketapi.dto.product.ProductDTO;
import com.klymenko.newmarketapi.dto.product.ProductUpdateDTO;
import com.klymenko.newmarketapi.entities.Product;
import com.klymenko.newmarketapi.service.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/product")
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody ProductDTO product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
    }

    @PatchMapping("/{productId}")
    public Product updateProduct(@Valid @RequestBody ProductUpdateDTO productUpdateDTO, @PathVariable String productId) {
        return productService.updateProduct(productUpdateDTO, productId);
    }
}
