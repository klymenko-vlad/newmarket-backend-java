package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.user.*;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.exceptions.BadRequestException;
import com.klymenko.newmarketapi.exceptions.ItemAlreadyExistsException;
import com.klymenko.newmarketapi.mappers.UserMapper;
import com.klymenko.newmarketapi.repository.UserRepository;
import com.klymenko.newmarketapi.security.JwtUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

        return userRepository.save(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authenticate;
        try {
             authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
                    );
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials");
        }
        System.out.println("end");
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.getUser(userId);
    }

    @Override
    public void updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        User user = getLoggedInUser();

        if (bCryptPasswordEncoder.matches(passwordUpdateDTO.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("The passwords are the same");
        }

        user.setPassword(bCryptPasswordEncoder.encode(passwordUpdateDTO.getNewPassword()));

        userRepository.updatePasswords(user);
    }


    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(UserUpdateDTO userUpdateDTO) {
        User user = getLoggedInUser();

        try {
            if (userUpdateDTO.getEmail() != null) {
                boolean emailExists = userRepository.existsByEmail(userUpdateDTO.getEmail());
                if (!emailExists) {
                    user.setEmail(userUpdateDTO.getEmail());
                } else {
                    throw new ItemAlreadyExistsException("Email %s is already taken".formatted(userUpdateDTO.getEmail()));
                }
            }

        } catch (EmptyResultDataAccessException e) {
            user.setEmail(userUpdateDTO.getEmail());
        }

        if (userUpdateDTO.getPictureUrl() != null) {
            user.setPictureUrl(userUpdateDTO.getPictureUrl());
        }
        if (userUpdateDTO.getName() != null) {
            user.setName(userUpdateDTO.getName());
        }

        System.out.println("no error");

        return userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(UserDeleteDTO userDeleteDTO) {
        User user = getLoggedInUser();

        if (!bCryptPasswordEncoder.matches(userDeleteDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Provide a right password");
        }

        userRepository.delete(user);
    }
}
