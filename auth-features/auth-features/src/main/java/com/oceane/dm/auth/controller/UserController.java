package com.oceane.dm.auth.controller;

import com.oceane.dm.auth.services.UserService;
import com.oceane.dm.models.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}
