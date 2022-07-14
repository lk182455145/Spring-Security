package com.lk.ss6.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("other")
public class OtherController {

    @GetMapping
    public String say() {
        return "this is other";
    }
}
