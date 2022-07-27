package com.lk.ss1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.ss1.filter.MyUsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    /**
     * security核心配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*****************基于form表单提交配置***********************/
        http.authorizeHttpRequests()
//                .antMatchers(HttpMethod.POST, "/login").permitAll()   // 下面写了loginProcessingUrl的这个请求地址，这句可以不用写了
                .antMatchers("/login.html").permitAll()
                .anyRequest().authenticated();
        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login"); // 登陆页面是login.html 登陆请求是login
        http.logout().logoutUrl("/logout"); //登出的路劲，默认是logout。登出后默认跳转到loginPage设定的路径(login.html)
        http.csrf().disable();

        /*****************基于json的配置***********************/
//        http.authorizeHttpRequests(req -> req
//                        .antMatchers(HttpMethod.POST, "/json/login").permitAll()
//                        .anyRequest().authenticated())
//                .addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .logout(logout -> logout.logoutUrl("/json/logout").logoutSuccessHandler(logoutSuccessHandler()))
//                .csrf().disable();
    }

    /**
     * 重写过滤器
     *
     * @return
     * @throws Exception
     */
    private MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter(objectMapper);
        myUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        myUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        myUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        myUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/json/login"); //确定哪个url才能进入这个filter
        return myUsernamePasswordAuthenticationFilter;
    }

    /**
     * 登录成功
     *
     * @return
     */
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (req, res, auth) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            res.setStatus(HttpStatus.OK.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.getWriter().println(objectMapper.writeValueAsString(auth));
            log.debug("认证成功");
        };
    }

    /**
     * 登录失败
     *
     * @return
     */
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

    /**
     * 登出成功
     *
     * @return
     */
    private LogoutSuccessHandler logoutSuccessHandler() {
        return (req, res, auth) -> {
            if (auth != null && auth.getDetails() != null) {
                req.getSession().invalidate();
            }
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println();
            log.debug("成功退出登录");
        };
    }

    /**
     * 不进入security filter chain过滤链来，意思就是不进行security的安全检查
     * 一般用于图片、视频、css等静态资源
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**");
    }

    /**
     * 基于用户认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("12345678"))
                .roles("USER", "ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
