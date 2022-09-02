package com.lk.ss6.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.ss6.authorization.DynamicAuthorizationManager;
import com.lk.ss6.handler.LoginFailureHandler;
import com.lk.ss6.handler.LoginSuccessHandler;
import com.lk.ss6.service.ResourceService;
import com.lk.ss6.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    private final UserService userService;

    private final ResourceService resourceService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request.anyRequest().access(new DynamicAuthorizationManager(resourceService)));
        http.formLogin().successHandler(loginSuccessHandler()).failureHandler(loginFailureHandler());
//        http.exceptionHandling(configurer -> { // 只有报AuthenticationException的异常才会进入此方法
//            MediaTypeRequestMatcher mediaTypeRequestMatcher = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
//            mediaTypeRequestMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
//            configurer.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), mediaTypeRequestMatcher);
//        });
        //在cookie里面显示csrf-token
//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.csrf();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(objectMapper);
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    }
}
