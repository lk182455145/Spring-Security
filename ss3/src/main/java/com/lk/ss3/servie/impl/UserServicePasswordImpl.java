package com.lk.ss3.servie.impl;

import com.lk.ss3.domain.User;
import com.lk.ss3.repository.UserRepository;
import com.lk.ss3.servie.UserServicePassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServicePasswordImpl implements UserServicePassword {

    private final UserRepository userRepository;

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        log.info("进入密码更新服务");
        if(userDetails instanceof User){
            User user = (User) userDetails;
            user.setPassword(newPassword);
            return userRepository.save(user);
        }
        return userDetails;
    }
}
