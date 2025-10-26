package com.hololitt.SpringBootProject.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginForm {
    public LoginForm(){}
    private String username;
    private String password;
}
