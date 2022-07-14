package com.lk.ss3.servie;

import com.lk.ss3.domain.User;
import com.lk.ss3.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(UserDto userDto);
}
