package com.lk.ss3.servie.impl;

import com.lk.ss3.domain.User;
import com.lk.ss3.dto.UserDto;
import com.lk.ss3.exception.ValidateException;
import com.lk.ss3.repository.RoleRepository;
import com.lk.ss3.repository.UserRepository;
import com.lk.ss3.servie.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户名错误"));
    }

    @Override
    public User register(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ValidateException("用户名存在");
        }
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .name(userDto.getName())
                .authorities(roleRepository.findByAuthority("ROLE_USER").stream().collect(Collectors.toSet()))
                .build();
        return userRepository.save(user);
    }
}
