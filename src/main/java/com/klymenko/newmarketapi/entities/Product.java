package com.klymenko.newmarketapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.klymenko.newmarketapi.enums.Categories;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private BigDecimal price;

    @Column(name = "past_price")
    private BigDecimal pastPrice;

    @Column(name = "main_picture_url")
    private String mainPictureUrl;

    @ElementCollection
    @Column(name = "pictures_url")
    private List<String> picturesUrl;

    private String description;

    private Integer quantity;

    private Categories category;

    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price) && Objects.equals(pastPrice, product.pastPrice) && Objects.equals(mainPictureUrl, product.mainPictureUrl) && Objects.equals(picturesUrl, product.picturesUrl) && Objects.equals(description, product.description) && Objects.equals(quantity, product.quantity) && category == product.category && Objects.equals(rating, product.rating) && Objects.equals(user, product.user) && Objects.equals(createdAt, product.createdAt) && Objects.equals(updatedAt, product.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, pastPrice, mainPictureUrl, picturesUrl, description, quantity, category, rating, user, createdAt, updatedAt);
    }
}