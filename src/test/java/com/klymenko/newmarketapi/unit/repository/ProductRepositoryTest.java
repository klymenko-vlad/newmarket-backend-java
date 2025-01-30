package com.klymenko.newmarketapi.unit.repository;

import com.klymenko.newmarketapi.entities.Product;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.enums.Categories;
import com.klymenko.newmarketapi.enums.Roles;
import com.klymenko.newmarketapi.exceptions.ResourceNotFoundException;
import com.klymenko.newmarketapi.repository.ProductRepository;
import com.klymenko.newmarketapi.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({ProductRepository.class, UserRepository.class})
public class ProductRepositoryTest {

    private User mockUser;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private User createMockUser() {
        return userRepository.save(
                User.builder()
                        .name("user")
                        .email("user@gmail.com")
                        .role(Roles.USER)
                        .password("test1234")
                        .build()
        );
    }

    private Product createMockProduct(String title, Categories category, BigDecimal price, int quantity, int rating, User user) {
        return Product.builder()
                .category(category)
                .price(price)
                .title(title)
                .mainPictureUrl("img.png")
                .quantity(quantity)
                .rating(rating)
                .user(user)
                .build();
    }

    @Test
    public void ProductRepository_DeleteProduct_RemovesProduct() {
        mockUser = createMockUser();

        Product mockProduct = createMockProduct("Title", Categories.FURNITURE, BigDecimal.valueOf(300), 3, 3, mockUser);
        Product savedProduct = productRepository.saveProduct(mockProduct);

        productRepository.deleteProduct(savedProduct.getId());

        Assertions.assertThatThrownBy(() -> productRepository.getProduct(savedProduct.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product with id");
    }

    @Test
    public void ProductRepository_UpdateProduct_UpdatesFields() {
        mockUser = createMockUser();

        Product mockProduct = createMockProduct("Old Title", Categories.FURNITURE, BigDecimal.valueOf(300), 3, 3, mockUser);
        Product savedProduct = productRepository.saveProduct(mockProduct);

        Product updatedProduct = Product.builder()
                .id(savedProduct.getId())
                .category(Categories.ACCESSORIES)
                .price(BigDecimal.valueOf(500))
                .title("Updated Title")
                .mainPictureUrl("updated_img.png")
                .quantity(10)
                .rating(5)
                .user(mockUser)
                .build();

        Product result = productRepository.updateProduct(updatedProduct);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(savedProduct.getId());
        Assertions.assertThat(result.getTitle()).isEqualTo("Updated Title");
        Assertions.assertThat(result.getCategory()).isEqualTo(Categories.ACCESSORIES);
        Assertions.assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(500));
        Assertions.assertThat(result.getQuantity()).isEqualTo(10);
        Assertions.assertThat(result.getRating()).isEqualTo(5);
    }

    @Test
    public void ProductRepository_GetProduct_ThrowsExceptionIfNotFound() {
        Assertions.assertThatThrownBy(() -> productRepository.getProduct("nonexistent-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product with id nonexistent-id is not found");
    }

    @Test
    public void ProductRepository_GetAllProducts_ReturnsEmptyListWhenNoProducts() {
        List<Product> products = productRepository.getAllProducts();

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).isEmpty();
    }

    @Test
    public void ProductRepository_SaveMultipleProducts_ReturnsAllSaved() {
        mockUser = createMockUser();

        Product product1 = createMockProduct("Product 1", Categories.ELECTRONICS, BigDecimal.valueOf(1000), 5, 4, mockUser);
        Product product2 = createMockProduct("Product 2", Categories.ACCESSORIES, BigDecimal.valueOf(2000), 10, 5, mockUser);

        productRepository.saveProduct(product1);
        productRepository.saveProduct(product2);

        List<Product> products = productRepository.getAllProducts();

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products.size()).isEqualTo(2);
        Assertions.assertThat(products)
                .extracting("title")
                .containsExactlyInAnyOrder("Product 1", "Product 2");
    }

    @Test
    public void ProductRepository_DeleteNonexistentProduct_ThrowsException() {
        Assertions.assertThatThrownBy(() -> productRepository.deleteProduct("nonexistent-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product with id nonexistent-id is not found");
    }
}
