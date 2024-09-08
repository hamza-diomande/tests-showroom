package com.oceane.dm.auth.controller;


import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.auth.services.TwoFactorUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users2Fa")
public class TwoFactorUserController {
    private final TwoFactorUserService userService;

    public TwoFactorUserController(TwoFactorUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public TwoFactorUser getCurrentUser() {
        return userService.getCurrentUser();
    }
}
