package com.lk.ss3.controller;

import com.lk.ss3.domain.User;
import com.lk.ss3.dto.UserDto;
import com.lk.ss3.servie.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("three")
public class ThreeController {

    private final UserService userService;

    @GetMapping("authentication")
    public Authentication getUserDetails(Authentication authentication) {
        return authentication;
    }

    @PostMapping("register")
    public User register(@RequestBody UserDto user) {
        return userService.register(user);
    }
}
