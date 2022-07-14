package com.lk.ss3.config;

import com.lk.ss3.provider.LDAPAuthenticationProvider;
import com.lk.ss3.repository.ldap.LDAPUserRepository;
import com.lk.ss3.servie.UserService;
import com.lk.ss3.servie.UserServicePassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final UserServicePassword userServicePassword;

    private final LDAPUserRepository ldapUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> req
                        .antMatchers(HttpMethod.POST, "/three/register").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 配置 LDAPAuthenticationProvider
                .authenticationProvider(ldapAuthenticationProvider())   // 给AuthenticationProvider增加一个provider
                // 配置 DaoAuthenticationProvider
                .userDetailsService(userService)    // 配置获取用户信息
                .passwordEncoder(passwordEncoder) // 配置密码验证
                .userDetailsPasswordManager(userServicePassword);   // 配置密码自动升级
    }

    @Bean
    public LDAPAuthenticationProvider ldapAuthenticationProvider() {
        LDAPAuthenticationProvider ldapAuthenticationProvider = new LDAPAuthenticationProvider(ldapUserRepository);
        return ldapAuthenticationProvider;
    }

    /**
     * 多密码编译解析器
     * @return
     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // 默认编码算法的 Id
//        String bcryptEncode = "bcrypt";
//        // 要支持的多种编码器
//        Map<String, PasswordEncoder> passwordMap = Map.of(
//                bcryptEncode, new BCryptPasswordEncoder(),
//                "SHA-1", new MessageDigestPasswordEncoder("SHA-1")
//        );
//        return new DelegatingPasswordEncoder(bcryptEncode, passwordMap);
//    }


//    public static void main(String[] args) {
//        System.out.println(new MessageDigestPasswordEncoder("SHA-1").encode("12345678"));
//    }
}
