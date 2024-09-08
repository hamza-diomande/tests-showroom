package com.oceane.dm.feedback.controller;

import com.oceane.dm.models.model.User;
import com.oceane.dm.feedback.service.UserFeedBackService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserFeedBackService userService;

    public UserController(UserFeedBackService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}
