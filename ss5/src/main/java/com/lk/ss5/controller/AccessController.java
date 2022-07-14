package com.lk.ss5.controller;

import com.lk.ss5.annotation.RoleAdminOrSelf;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("access")
public class AccessController {

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "this is admin";
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "this is user";
    }

    @GetMapping("any")
    @PreAuthorize("hasAnyRole('ADMIN','USER','OTHER')")
    public String any() {
        return "this is any";
    }

    @GetMapping("name/{username}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #username")
    public String name(@PathVariable("username")String username) {
        return "this is name = " + username;
    }

    @GetMapping("name2/{username}")
    @RoleAdminOrSelf
    public String name2(@PathVariable("username")String username) {
        return "this is name2 = " + username;
    }



}
