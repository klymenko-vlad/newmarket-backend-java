package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.product.ProductDTO;
import com.klymenko.newmarketapi.dto.product.ProductUpdateDTO;
import com.klymenko.newmarketapi.entities.Product;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product createProduct(ProductDTO productDTO);

    List<Product> getAllProducts();

    Product getProductById(String productId);

    void deleteProduct(String productId);

    Product updateProduct(@Valid ProductUpdateDTO productUpdateDTO, String productId);

    List<Product> getProductByKeyword(String keyword);
}
