package com.klymenko.newmarketapi.controller;

import com.klymenko.newmarketapi.dto.user.PasswordUpdateDTO;
import com.klymenko.newmarketapi.dto.user.UserDeleteDTO;
import com.klymenko.newmarketapi.dto.user.UserUpdateDTO;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {

        return userService.getUserById(userId);
    }

    @PostMapping()
    public void updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        userService.updatePassword(passwordUpdateDTO);
    }

    @PatchMapping()
    public User updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userUpdateDTO);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Valid @RequestBody UserDeleteDTO userDeleteDTO) {
        userService.deleteUser(userDeleteDTO);
    }

}
