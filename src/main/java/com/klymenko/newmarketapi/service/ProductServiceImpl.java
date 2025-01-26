package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.product.ProductDTO;
import com.klymenko.newmarketapi.dto.product.ProductUpdateDTO;
import com.klymenko.newmarketapi.entities.Product;
import com.klymenko.newmarketapi.mappers.ProductMapper;
import com.klymenko.newmarketapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserServiceImpl userService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, UserServiceImpl userService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userService = userService;
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        System.out.println(productDTO);
        Product product = productMapper.mapToProductEntity(productDTO);
        product.setUser(userService.getLoggedInUser());

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

    @Override
    public Product updateProduct(ProductUpdateDTO productUpdateDTO, String productId) {
        Product newProduct = getProductById(productId);

        if (productUpdateDTO.getDescription() != null && !productUpdateDTO.getDescription().equals(newProduct.getDescription())) {
            newProduct.setDescription(productUpdateDTO.getDescription());
        }
        if (productUpdateDTO.getPrice() != null && !productUpdateDTO.getPrice().equals(newProduct.getPrice())) {
            newProduct.setPrice(productUpdateDTO.getPrice());
        }
        if (productUpdateDTO.getCategory() != null && !productUpdateDTO.getCategory().equals(newProduct.getCategory())) {
            newProduct.setCategory(productUpdateDTO.getCategory());
        }
        if (productUpdateDTO.getQuantity() != null && !productUpdateDTO.getQuantity().equals(newProduct.getQuantity())) {
            newProduct.setQuantity(productUpdateDTO.getQuantity());
        }
        if (productUpdateDTO.getRating() != null && !productUpdateDTO.getRating().equals(newProduct.getRating())) {
            newProduct.setRating(productUpdateDTO.getRating());
        }
        if (productUpdateDTO.getMainPictureUrl() != null && !productUpdateDTO.getMainPictureUrl().equals(newProduct.getMainPictureUrl())) {
            newProduct.setMainPictureUrl(productUpdateDTO.getMainPictureUrl());
        }
        if (productUpdateDTO.getPastPrice() != null && !productUpdateDTO.getPastPrice().equals(newProduct.getPastPrice())) {
            newProduct.setPastPrice(productUpdateDTO.getPastPrice());
        }
        if (productUpdateDTO.getTitle() != null && !productUpdateDTO.getTitle().equals(newProduct.getTitle())) {
            newProduct.setTitle(productUpdateDTO.getTitle());
        }
        if (productUpdateDTO.getPicturesUrl() != null && !productUpdateDTO.getPicturesUrl().equals(newProduct.getPicturesUrl())) {
            newProduct.setPicturesUrl(productUpdateDTO.getPicturesUrl());
        }


        return productRepository.updateProduct(newProduct);
    }
}
