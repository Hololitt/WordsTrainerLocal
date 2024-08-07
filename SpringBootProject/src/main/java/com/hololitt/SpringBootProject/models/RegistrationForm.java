package com.hololitt.SpringBootProject.models;

public class RegistrationForm {
    public RegistrationForm(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
public RegistrationForm(){}
    private String name;
    private String password;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
