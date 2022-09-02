package com.lk.ss6.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("access")
public class AccessController {

    @GetMapping
    public String sayGet() {
        return "this is get access";
    }

    @PostMapping
    public String sayPost() {
        return "this is post access";
    }
    
}
