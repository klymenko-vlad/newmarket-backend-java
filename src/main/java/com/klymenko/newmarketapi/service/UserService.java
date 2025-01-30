package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.user.*;
import com.klymenko.newmarketapi.entities.User;
import jakarta.validation.Valid;

public interface UserService {
    User createUser(UserDTO userDTO);

    String login(LoginDTO loginDTO);

    User getUserById(Long userId);

    void updatePassword(@Valid PasswordUpdateDTO passwordUpdateDTO);

    User getLoggedInUser();

    User updateUser(@Valid UserUpdateDTO userUpdateDTO);

    void deleteUser(UserDeleteDTO userDeleteDTO);

    User getUserByEmail(String email);
}
