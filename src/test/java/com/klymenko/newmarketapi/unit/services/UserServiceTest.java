package com.klymenko.newmarketapi.unit.services;

import com.klymenko.newmarketapi.dto.user.LoginDTO;
import com.klymenko.newmarketapi.dto.user.UserDTO;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.enums.Roles;
import com.klymenko.newmarketapi.exceptions.ResourceNotFoundException;
import com.klymenko.newmarketapi.mappers.UserMapper;
import com.klymenko.newmarketapi.repository.UserRepository;
import com.klymenko.newmarketapi.security.JwtUtil;
import com.klymenko.newmarketapi.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetails userDetails;

    @Mock
    private JwtUtil jwtUtils;

    @InjectMocks
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

        when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        when(userMapper.mapToUserEntity(Mockito.any(UserDTO.class))).thenReturn(mockUser);

        User user = userService.createUser(mockUserDto);

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void UserService_GetUserById_ReturnsUser() {
        User mockUser = User.builder()
                .name("user")
                .email("user@gmail.com")
                .role(Roles.USER)
                .password("test1234")
                .build();

        when(userRepository.getUser(Mockito.any(Long.class))).thenReturn(mockUser);

        User user = userService.getUserById(2L);

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void UserService_GetUserById_NotFound() {
        when(userRepository.getUser(Mockito.any(Long.class))).thenThrow(new ResourceNotFoundException("User with id %s isn't found".formatted(Mockito.any(Long.class))));

        Assertions.assertThatThrownBy(() -> userService.getUserById(2L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void UserService_Login_Success() {
        String mockEmail = "test@gmail.com";
        String mockJwt = "jwt-token";
        LoginDTO loginDTO = LoginDTO.builder().email(mockEmail).password("test1234").build();

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        )).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(userDetails.getUsername()).thenReturn(mockEmail);

        when(jwtUtils.generateToken(userDetails.getUsername())).thenReturn(mockJwt);

        String token = userService.login(loginDTO);

        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token).isEqualTo(mockJwt);
        verify(authenticationManager, times(1))
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateToken(mockEmail);
    }

    @Test
    public void UserService_Login_WrongPasswordOrEmail() {
        String mockEmail = "test@gmail.com";
        LoginDTO loginDTO = LoginDTO.builder().email(mockEmail).password("test1234").build();

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        )).thenThrow(new BadCredentialsException("Bad credentials"));

        Assertions.assertThatThrownBy(() -> userService.login(loginDTO)).isInstanceOf(BadCredentialsException.class);
    }
}
