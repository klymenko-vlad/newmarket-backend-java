package com.klymenko.newmarketapi.controller;

import com.klymenko.newmarketapi.dto.LoginDTO;
import com.klymenko.newmarketapi.dto.UserDTO;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.io.JwtResponse;
import com.klymenko.newmarketapi.security.JwtUtil;
import com.klymenko.newmarketapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<>(new JwtResponse(userService.login(loginDTO)), HttpStatus.OK);
    }

}
