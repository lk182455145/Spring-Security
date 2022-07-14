package com.lk.ss4.controller;

import com.lk.ss4.dto.Token;
import com.lk.ss4.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("four")
@RequiredArgsConstructor
public class FourController {

    private final JwtUtil jwtUtil;

    @GetMapping("authentication")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

    @PostMapping(value = "refreshToken", headers = "Authorization")
    public Token refreshToken(@RequestHeader("Authorization") String authorization, @RequestBody Token token) {
        if(jwtUtil.validateToken(token.getRefreshToken())) {
            return token.withAccessToken(jwtUtil.buildAccessTokenWithRefreshToken(token.getRefreshToken()));
        }
        throw new AccessDeniedException("token 错误");
    }
}
