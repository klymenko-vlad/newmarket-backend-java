package com.klymenko.newmarketapi.repository;

import com.klymenko.newmarketapi.entities.User;
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
        Optional<User> optionalUser = Optional.ofNullable(entityManager.createQuery("from User where email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult());

        System.out.println(optionalUser);

        return optionalUser;
    }

    public Boolean existsByEmail(String email) {
        return entityManager.createQuery("select (count(*) > 0) from User where email = :email", Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User getUser(Long id) {
        return entityManager.find(User.class, id);
    }

    @Transactional
    public User save(User user) {
        entityManager.persist(user);

        return getUser(user.getId());
    }
}
