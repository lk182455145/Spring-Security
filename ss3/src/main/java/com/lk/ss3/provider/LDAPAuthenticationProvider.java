package com.lk.ss3.provider;

import com.lk.ss3.repository.ldap.LDAPUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 展示和 DaoAuthenticationProvider 一起工作的场景
 * 使用 UsernamePasswordAuthenticationToken
 */
@RequiredArgsConstructor
public class LDAPAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final LDAPUserRepository ldapUserRepository;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return ldapUserRepository.findByUsernameAndPassword(username,authentication.getCredentials().toString()).orElseThrow(() -> new BadCredentialsException("[LDAP]用户名或密码错误"));
    }
}
