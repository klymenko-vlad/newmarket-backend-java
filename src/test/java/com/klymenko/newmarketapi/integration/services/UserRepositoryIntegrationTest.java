package com.klymenko.newmarketapi.integration.services;

import com.klymenko.newmarketapi.config.WebSecurityConfig;
import com.klymenko.newmarketapi.dto.user.LoginDTO;
import com.klymenko.newmarketapi.dto.user.UserDTO;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.enums.Roles;
import com.klymenko.newmarketapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.newmarketapi.mappers.UserMapperImpl;
import com.klymenko.newmarketapi.repository.UserRepository;
import com.klymenko.newmarketapi.security.AuthEntryPointJwt;
import com.klymenko.newmarketapi.security.CustomUserDetailsService;
import com.klymenko.newmarketapi.security.JwtUtil;
import com.klymenko.newmarketapi.service.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@Import({AuthEntryPointJwt.class, CustomUserDetailsService.class, UserRepository.class, UserServiceImpl.class, UserMapperImpl.class, WebSecurityConfig.class, JwtUtil.class})
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void UserService_CreateUser_ReturnsUser() {
        User mockUser = User.builder()
                .name("user")
                .email("user@gmail.com")
                .role(Roles.USER)
                .password("test1234")
                .build();

        UserDTO mockUserDto = UserDTO.builder().name("user")
                .email("user@gmail.com")
                .role("USER")
                .password("test1234")
                .build();

        User user = userService.createUser(mockUserDto);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getEmail()).isEqualTo(mockUser.getEmail());
    }

    @Test
    public void UserService_CreateTwoTheSameUser_ThrowAnError() {
        UserDTO mockUserDto = UserDTO.builder().name("user")
                .email("user@gmail.com")
                .role("USER")
                .password("test1234")
                .build();

        userService.createUser(mockUserDto);

        Assertions.assertThatThrownBy(() -> userService.createUser(mockUserDto)).isInstanceOf(ItemAlreadyExistsException.class);
    }

    @Test
    public void UserService_LoginWithValidCredentials_ReturnsToken() {
        UserDTO mockUserDto = UserDTO.builder().name("user")
                .email("user@gmail.com")
                .role("USER")
                .password("test1234")
                .build();

        User user = userService.createUser(mockUserDto);

        LoginDTO loginDTO = new LoginDTO(user.getEmail(), mockUserDto.getPassword());

        String token = userService.login(loginDTO);

        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void UserService_LoginWithNotValidCredentials_ThrowAnError() {
        UserDTO mockUserDto = UserDTO.builder().name("user")
                .email("user@gmail.com")
                .role("USER")
                .password("test1234")
                .build();

        User user = userService.createUser(mockUserDto);

        LoginDTO loginDTO = new LoginDTO(user.getEmail(), "wrong password");

        Assertions.assertThatThrownBy(() -> userService.login(loginDTO)).isInstanceOf(
                BadCredentialsException.class
        );

    }
}
