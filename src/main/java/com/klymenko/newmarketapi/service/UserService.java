package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.LoginDTO;
import com.klymenko.newmarketapi.dto.UserDTO;
import com.klymenko.newmarketapi.entities.User;

public interface UserService {
    User createUser(UserDTO userDTO);

    String login(LoginDTO loginDTO);
}
