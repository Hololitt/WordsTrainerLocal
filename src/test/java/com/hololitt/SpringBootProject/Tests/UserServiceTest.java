package com.hololitt.SpringBootProject.Tests;

import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.repositorys.UserRepository;
import com.hololitt.SpringBootProject.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Mock
   private UserRepository userRepository;

    @InjectMocks
private UserService userService;


@BeforeEach
    public void prepareMocks(){
    MockitoAnnotations.openMocks(this);
}

@Test
    public void testGetAllUsers(){
List<User> users = new ArrayList<>();

users.add(new User("name", "asdd", "email"));
    users.add(new User("name2", "asddasf", "email2"));

    when(userRepository.findAll()).thenReturn(users);

    assertEquals(2, userService.getAllUsers().size());
}
}