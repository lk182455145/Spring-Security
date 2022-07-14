package com.lk.ss1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("one")
public class OneController {

    @GetMapping
    public String sayHello() {
        return "Hello Spring Security";
    }
}
