package com.lk.ss5.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security默认是禁用注解的，要想开启注解，需要在继承WebSecurityConfigurerAdapter的类上加@EnableGlobalMethodSecurity注解。
 * <br/>
 * <p>
 * <p>
 * {@link EnableGlobalMethodSecurity}的几种方法
 * <li>{@link EnableGlobalMethodSecurity#prePostEnabled}: 确定 前置注解[@PreAuthorize,@PostAuthorize,@PreFilter,@PostFilter] 是否启用</li>
 * <li>{@link EnableGlobalMethodSecurity#securedEnabled}: 确定安全注解 [@Secured] 是否启用</li>
 * <li>{@link EnableGlobalMethodSecurity#jsr250Enabled}: 确定 JSR-250注解 [@RolesAllowed..]是否启用</li>
 * <br/>
 * <p>
 * <p>
 * 内置表达式
 * <li>hasRole([role]): 当前用户是否拥有指定角色。</li>
 * <li>hasAnyRole([role1,role2]): 多个角色是一个以逗号进行分隔的字符串。如果当前用户拥有指定角色中的任意一个则返回true。</li>
 * <li>hasAuthority([auth]): 等同于hasRole,不过要加上前缀ROLE_</li>
 * <li>hasAnyAuthority([auth1,auth2]): 等同于hasAnyRole,不过要加上前缀ROLE_</li>
 * <li>principal: 代表当前用户的principle对象 {@link org.springframework.security.core.userdetails.UserDetails}</li>
 * <li>authentication: 直接从SecurityContext获取的当前Authentication对象 {@link org.springframework.security.core.Authentication}</li>
 * <li>permitAll: 总是返回true，表示允许所有的</li>
 * <li>denyAll: 总是返回false，表示拒绝所有的</li>
 * <li>isAnonymous(): 当前用户是否是一个匿名用户</li>
 * <li>isRememberMe(): 表示当前用户是否是通过Remember-Me自动登录的</li>
 * <li>isAuthenticated(): 表示当前用户是否已经登录认证成功了</li>
 * <li>isFullyAuthenticated(): 如果当前用户既不是一个匿名用户，同时又不是通过Remember-Me自动登录的，则返回true。</li>
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request.anyRequest().authenticated());
        http.formLogin();
        http.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("123456"))
                .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 角色层级划分
     * @return
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\nROLE_USER > ROLE_OTHER"); // ADMIN角色包含USER角色,USER角色包含OTHER角色
        return roleHierarchy;
    }
}
