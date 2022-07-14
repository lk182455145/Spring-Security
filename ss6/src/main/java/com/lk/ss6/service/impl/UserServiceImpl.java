package com.lk.ss6.service.impl;

import com.lk.ss6.respotiry.UserRespotiry;
import com.lk.ss6.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRespotiry userRespotiry;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRespotiry.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户名错误"));
    }
}
