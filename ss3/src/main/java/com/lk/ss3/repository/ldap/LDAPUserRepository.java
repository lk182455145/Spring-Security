package com.lk.ss3.repository.ldap;

import com.lk.ss3.domain.ldap.LDAPUser;
import org.springframework.data.ldap.repository.LdapRepository;

import java.util.Optional;

public interface LDAPUserRepository extends LdapRepository<LDAPUser> {

    Optional<LDAPUser> findByUsernameAndPassword(String username, String password);
}
