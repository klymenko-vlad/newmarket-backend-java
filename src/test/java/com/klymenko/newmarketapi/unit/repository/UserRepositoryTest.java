package com.klymenko.newmarketapi.unit.repository;

import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.enums.Roles;
import com.klymenko.newmarketapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.newmarketapi.exceptions.ResourceNotFoundException;
import com.klymenko.newmarketapi.repository.UserRepository;
import jakarta.persistence.NoResultException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(UserRepository.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    // Saving User
    @Test
    public void UserRepository_Save_ReturnsSavedUser() {
        User mockUser = User.builder()
                .name("user")
                .email("user@gmail.com")
                .role(Roles.USER)
                .password("test1234")
                .build();

        User savedUser = userRepository.save(mockUser);
        System.out.println(savedUser);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    // Getting By ID
    @Test
    public void UserRepository_GetUser_ReturnsUser() {
        User mockUser = User.builder()
                .name("user")
                .email("user@gmail.com")
                .role(Roles.USER)
                .password("test1234")
                .build();

        User saved = userRepository.save(mockUser);

        User user = userRepository.getUser(saved.getId());

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    // Deleting User
    @Test
    public void UserRepository_DeleteUser_ThrowsResourceNotFoundException() {
        User mockUser = User.builder()
                .name("user")
                .email("user@gmail.com")
                .role(Roles.USER)
                .password("test1234")
                .build();

        User user = userRepository.save(mockUser);

        userRepository.delete(user);

        Assertions.assertThatThrownBy(() -> userRepository.getUser(user.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // Testing findByEmail
    @Test
    public void UserRepository_FindByEmail_Found() {
        String email = "user@gmail.com";
        User mockUser = User.builder()
                .name("user")
                .email(email)
                .role(Roles.USER)
                .password("test1234")
                .build();

        userRepository.save(mockUser);

        var result = userRepository.findByEmail(email);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    public void UserRepository_FindByEmail_NotFound() {
        String email = "nonexistent@gmail.com";

        Assertions.assertThatThrownBy(() -> userRepository.findByEmail(email))
                .isInstanceOf(NoResultException.class);
    }

    // Testing existsByEmail
    @Test
    public void UserRepository_ExistsByEmail_True() {
        String email = "user@gmail.com";
        User mockUser = User.builder()
                .name("user")
                .email(email)
                .role(Roles.USER)
                .password("test1234")
                .build();

        userRepository.save(mockUser);

        Boolean result = userRepository.existsByEmail(email);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void UserRepository_ExistsByEmail_False() {
        String email = "nonexistent@gmail.com";

        Boolean result = userRepository.existsByEmail(email);

        Assertions.assertThat(result).isFalse();
    }

    // Testing updatePasswords
    @Test
    public void UserRepository_UpdatePasswords_Success() {
        User mockUser = User.builder()
                .name("user")
                .email("user@gmail.com")
                .role(Roles.USER)
                .password(bCryptPasswordEncoder.encode("test1234"))
                .build();

        User savedUser = userRepository.save(mockUser);

        savedUser.setPassword("newpassword123");

        userRepository.updatePasswords(savedUser);

        User updatedUser = userRepository.getUser(savedUser.getId());

        Assertions.assertThat(updatedUser.getPassword()).isEqualTo("newpassword123");
    }

    // Testing updateUser
    @Test
    public void UserRepository_UpdateUser_Success() {
        User mockUser = User.builder()
                .name("user")
                .email("user@gmail.com")
                .role(Roles.USER)
                .password("test1234")
                .build();

        User savedUser = userRepository.save(mockUser);

        savedUser.setName("updated user");
        savedUser.setPassword("updatedpassword");

        User updatedUser = userRepository.updateUser(savedUser);

        Assertions.assertThat(updatedUser.getName()).isEqualTo("updated user");
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo("updatedpassword");
    }

    // Testing save with existing email
    @Test
    public void UserRepository_Save_UserAlreadyExists() {
        String email = "user@gmail.com";
        User mockUser = User.builder()
                .name("user")
                .email(email)
                .role(Roles.USER)
                .password("test1234")
                .build();

        userRepository.save(mockUser);

        User duplicateUser = User.builder()
                .name("another user")
                .email(email)
                .role(Roles.USER)
                .password("test1234")
                .build();

        Assertions.assertThatThrownBy(() -> userRepository.save(duplicateUser))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessage("User is already exist with email user@gmail.com");
    }
}
