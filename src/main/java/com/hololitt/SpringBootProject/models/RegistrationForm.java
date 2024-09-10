package com.hololitt.SpringBootProject.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
