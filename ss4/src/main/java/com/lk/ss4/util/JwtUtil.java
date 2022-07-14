package com.lk.ss4.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 用于签名 Token

    public static final long ACCESS_TOKEN_EXPIRETIME = 10 * 60 * 1000L; // Access Token 过期时间
    public static final long REFRESH_TOKEN_EXPIRETIME = 24 * 60 * 60 * 1000L; // Refresh Token 过期时间

    public static final String HEADER = "Authorization"; // HTTP 报头的认证字段的 key
    public static final String PREFIX = "Bearer "; // HTTP 报头的认证字段的值的前缀

    private final ObjectMapper objectMapper;

    /**
     * 根据用户信息生成一个 JWT
     *
     * @param userDetails  用户信息
     * @param timeToExpire 毫秒单位的失效时间
     * @param signKey      签名使用的 key
     * @return JWT
     */
    private String createJWTToken(UserDetails userDetails, long timeToExpire, Key signKey) throws JsonProcessingException {
        if(userDetails == null) return null;
        return Jwts
                .builder()
                .setId("lk")
                .setSubject(objectMapper.writeValueAsString(userDetails))
                .claim("authorities",
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + timeToExpire))
                .signWith(signKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 创建TOKEN
     * @param userDetails
     * @return
     * @throws JsonProcessingException
     */
    public String createAccessToken(UserDetails userDetails) throws JsonProcessingException {
        return createJWTToken(userDetails, ACCESS_TOKEN_EXPIRETIME, KEY);
    }

    /**
     * 创建刷新TOKEN
     * @param userDetails
     * @return
     * @throws JsonProcessingException
     */
    public String createRefreshToken(UserDetails userDetails) throws JsonProcessingException {
        return createJWTToken(userDetails, REFRESH_TOKEN_EXPIRETIME, KEY);
    }

    /**
     * 刷新TOKEN
     * @param jwtToken
     * @return
     */
    public String buildAccessTokenWithRefreshToken(String jwtToken) {
        return parseClaims(jwtToken)
                .map(claims -> Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRETIME))
                        .signWith(KEY, SignatureAlgorithm.HS512).compact())
                .orElseThrow();
    }

    public Optional<Claims> parseClaims(String jwtToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(jwtToken).getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 验证TOKEN
     * @param jwtToken
     * @return
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
