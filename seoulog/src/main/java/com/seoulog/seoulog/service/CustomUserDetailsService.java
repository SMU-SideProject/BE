package com.seoulog.seoulog.service;

import com.seoulog.seoulog.config.auth.PrincipalDetails;
import com.seoulog.seoulog.dto.UserDto;
import com.seoulog.seoulog.entity.User;
import com.seoulog.seoulog.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        User userEntity = userRepository.findOneWithAuthoritiesByEmail(email);
        if (userEntity != null) {
            return new PrincipalDetails(userEntity); //userEntity를 넣어줘야 UserDetails에서 우리의 User 객체를 사용할 수 있음
        }
        return null;
    }

    private PrincipalDetails createUser(String username, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }

        return new PrincipalDetails(user);
    }
}