package com.hololitt.SpringBootProject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegistrationForm {
public RegistrationForm(){}
    private String name;
    private String password;
    private String email;

}
