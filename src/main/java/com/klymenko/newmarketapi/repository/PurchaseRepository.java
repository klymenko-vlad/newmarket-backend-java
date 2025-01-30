package com.klymenko.newmarketapi.repository;

import com.klymenko.newmarketapi.entities.Purchase;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseRepository {

    private final EntityManager entityManager;

    public PurchaseRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(Purchase purchase) {
        entityManager.persist(purchase);
    }
}
