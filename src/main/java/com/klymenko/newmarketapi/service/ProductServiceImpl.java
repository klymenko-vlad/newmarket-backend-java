package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.ProductDTO;
import com.klymenko.newmarketapi.entities.Product;
import com.klymenko.newmarketapi.mappers.ProductMapper;
import com.klymenko.newmarketapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        System.out.println(productDTO);
        Product product = productMapper.mapToProductEntity(productDTO);
        System.out.println(product);

        return productRepository.saveProduct(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductById(String productId) {
        return productRepository.getProduct(productId);
    }

    @Override
    public void deleteProduct(String productId) {
        productRepository.deleteProduct(productId);
    }
}
