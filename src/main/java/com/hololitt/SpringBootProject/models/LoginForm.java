package com.hololitt.SpringBootProject.models;

public class LoginForm {
    public LoginForm(){}
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }
public String getPassword(){return password;}

    public void setUsername(String name) {
        this.username = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
