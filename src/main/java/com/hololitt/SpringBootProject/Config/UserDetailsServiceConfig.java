package com.hololitt.SpringBootProject.Config;

import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@SuppressWarnings("unused")
public class UserDetailsServiceConfig {
    private final UserService userService;

    @Autowired
    public UserDetailsServiceConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {

            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }

            User user = userService.findUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            return new CustomUserDetails(
                    user.getName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getId(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}