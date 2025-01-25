package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.user.LoginDTO;
import com.klymenko.newmarketapi.dto.user.UserDTO;
import com.klymenko.newmarketapi.entities.User;

public interface UserService {
    User createUser(UserDTO userDTO);

    String login(LoginDTO loginDTO);
}
