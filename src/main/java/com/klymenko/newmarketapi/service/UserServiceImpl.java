package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.user.LoginDTO;
import com.klymenko.newmarketapi.dto.user.UserDTO;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.newmarketapi.mappers.UserMapper;
import com.klymenko.newmarketapi.repository.UserRepository;
import com.klymenko.newmarketapi.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = userMapper.mapToUserEntity(userDTO);

        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ItemAlreadyExistsException("User is already exist with email %s".formatted(user.getEmail()));
        }

        return userRepository.save(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
                );

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }
}
