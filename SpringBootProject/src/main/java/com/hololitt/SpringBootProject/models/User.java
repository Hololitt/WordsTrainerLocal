package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "users")
public class User {
    public User(){}
    public User(String name, String password, String email, long id){
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
    }
    public User(String name, String password, String email){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    @Column(name = "username")
    private String name;
    @Column(name = "password")
    private String password;
    @Email
    @Column(name = "email")
    private String email;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public long getId(){
        return id;
    }
public void setPassword(String password){
        this.password = password;
}

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
