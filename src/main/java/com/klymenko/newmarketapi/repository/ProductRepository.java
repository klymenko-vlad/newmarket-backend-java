package com.klymenko.newmarketapi.repository;

import com.klymenko.newmarketapi.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return entityManager.find(Product.class, productId);
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
}
