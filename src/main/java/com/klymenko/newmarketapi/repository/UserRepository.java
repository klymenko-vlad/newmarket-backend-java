package com.klymenko.newmarketapi.repository;

import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final EntityManager entityManager;


    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(entityManager.createQuery("from User where email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult());
    }

    public Boolean existsByEmail(String email) {
        return entityManager.createQuery("select (count(*) > 0) from User where email = :email", Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User getUser(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("User with id %s isn't found".formatted(id)));
    }

    @Transactional
    public User save(User user) {
        entityManager.persist(user);

        return getUser(user.getId());
    }

    @Transactional
    public void updatePasswords(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public User updateUser(User user) {
        entityManager.merge(user);

        return getUser(user.getId());
    }

    @Transactional
    public void delete(User user) {
        entityManager.remove(user);
    }
}
