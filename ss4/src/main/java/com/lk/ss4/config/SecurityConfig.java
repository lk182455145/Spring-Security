package com.lk.ss4.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.ss4.dto.Token;
import com.lk.ss4.filter.JWTFilter;
import com.lk.ss4.filter.MyUsernamePasswordAuthenticationFilter;
import com.lk.ss4.service.UserService;
import com.lk.ss4.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    private final JWTFilter jwtFilter;

    private final JwtUtil jwtUtil;

    private final Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request.mvcMatchers("/four/refreshToken").permitAll().anyRequest().authenticated())
                .addFilterBefore(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, MyUsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf().disable();
        http.formLogin().successHandler(authenticationSuccessHandler()).failureHandler(authenticationFailureHandler());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/json/login");
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String bcryptEncode = "bcrypt";
        Map<String, PasswordEncoder> passwordMap = Map.of(
                bcryptEncode, new BCryptPasswordEncoder(),
                "SHA-1", new MessageDigestPasswordEncoder("SHA-1")
        );
        return new DelegatingPasswordEncoder(bcryptEncode, passwordMap);
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (req, res, exc) -> {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            Map<String, String> errData = Map.of(
                    "title", "认证失败",
                    "details", exc.getMessage()
            );
            res.getWriter().println(objectMapper.writeValueAsString(errData));
        };
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (req, res, auth) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            res.setStatus(HttpStatus.OK.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            UserDetails userDetails = null;
            if (auth.getPrincipal() instanceof UserDetails) {
                userDetails = (UserDetails) auth.getPrincipal();
            }
            Token token = Token.builder().accessToken(jwtUtil.createAccessToken(userDetails)).refreshToken(jwtUtil.createRefreshToken(userDetails)).build();
//            Map<String, String> toke = Map.of("toke", jwtUtil.createAccessToken(userDetails), "refreshToken", jwtUtil.createRefreshToken(userDetails));
            res.getWriter().println(objectMapper.writeValueAsString(token));
            log.debug("认证成功");
        };
    }

    /**
     * 设施跨域访问
     *
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (environment.acceptsProfiles(Profiles.of("dev"))) { //开发环境
            configuration.addAllowedOrigin("http://localhost:8000");
        } else { // 正式环境
            configuration.addAllowedOrigin("http://www.lk.com");
        }
        configuration.setAllowedHeaders(Arrays.asList("GET", "POST", "DELETE", "PUT")); // 设置允许跨域的方法
        configuration.setAllowedHeaders(Collections.singletonList("*")); // 设置允许跨域的请求头，*代表所有
        configuration.addExposedHeader("X-Authenticate"); // 设置自定义返回的响应头
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对configuration的配置运用在所有URL上
        return source;
    }

}
