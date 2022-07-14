package com.lk.ss4.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.ss4.domain.Role;
import com.lk.ss4.domain.User;
import com.lk.ss4.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkJwtToken(request)) {
            validateToken(request)
                    .filter(claims -> claims.getId() != null)
                    .ifPresentOrElse(
                            this::setAuthentication,
                            SecurityContextHolder::clearContext);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 检查 JWT Token 是否在 HTTP 报头中
     *
     * @param req HTTP 请求
     * @return 是否有 JWT Token
     */
    private boolean checkJwtToken(HttpServletRequest req) {
        String authenticationHeader = req.getHeader(JwtUtil.HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(JwtUtil.PREFIX);
    }

    private Optional<Claims> validateToken(HttpServletRequest req) {
        String jwtToken = req.getHeader(JwtUtil.HEADER).replace(JwtUtil.PREFIX, "");
        try {
            return Optional.of(Jwts.parserBuilder().setSigningKey(JwtUtil.KEY).build().parseClaimsJws(jwtToken).getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void setAuthentication(Claims claims) {
        List<?> authorities = convertObjectToList(claims.get("authorities"));
        List<Role> roles = authorities.stream()
                .map(String::valueOf)
                .map(Role::new)
                .collect(Collectors.toList());
        try {
            User user = objectMapper.readValue(claims.getSubject(), User.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, roles);
            SecurityContextHolder.getContext().setAuthentication(token);
        } catch (JsonProcessingException e) {
            throw new BadCredentialsException("JWT认证失败");
        }

    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj instanceof Object[]) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }
}
