package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.Config.CustomUserDetails;
import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }

        return null;
    }
    public long getUserId(){
        return getCurrentUser().getId();
    }
    public void addUser(User user){
String encodedPassword = passwordEncoder.encode(user.getPassword());
user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
    public User findUserByUsername(String username){
        return userRepository.findByName(username);
    }
    public boolean isUserExist(String username){
       return userRepository.existsByName(username);
    }
    public void updatePasswordForUser(String username, String newPassword) {
        User user = userRepository.findByName(username);
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
