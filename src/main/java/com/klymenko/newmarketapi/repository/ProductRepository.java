package com.klymenko.newmarketapi.repository;

import com.klymenko.newmarketapi.entities.Product;
import com.klymenko.newmarketapi.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Product> getAllProducts() {
        return entityManager.createQuery("from Product", Product.class).getResultList();
    }

    public Product getProduct(String productId) {
        return Optional.ofNullable(entityManager.find(Product.class, productId))
                .orElseThrow(() -> new ResourceNotFoundException("Product with id %s is not found"
                        .formatted(productId))
                );
    }

    @Transactional
    public Product saveProduct(Product product) {
        entityManager.persist(product);

        return getProduct(product.getId());
    }

    @Transactional
    public void deleteProduct(String productId) {
        Product product = getProduct(productId);

        entityManager.remove(product);
    }

    @Transactional
    public Product updateProduct(Product newProduct) {
        entityManager.merge(newProduct);

        return getProduct(newProduct.getId());
    }

    public List<Product> getProductByKeyword(String keyword) {
        return entityManager.createQuery(
                        "from Product where title ILIKE :keyword or description ILIKE :keyword", Product.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }
}
