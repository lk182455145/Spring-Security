package com.lk.ss1.controller;

import com.lk.ss1.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("one")
@RequiredArgsConstructor
public class OneController {

    private final Config config;

    @GetMapping
    public String sayHello() {
        return "Hello Spring Security" + config.getName() + config.getMessage();
    }
}
