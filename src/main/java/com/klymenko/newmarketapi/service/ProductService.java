package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.ProductDTO;
import com.klymenko.newmarketapi.entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product createProduct(ProductDTO productDTO);

    List<Product> getAllProducts();

    Product getProductById(String productId);

    void deleteProduct(String productId);
}
