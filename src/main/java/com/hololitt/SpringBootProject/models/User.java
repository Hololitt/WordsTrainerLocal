package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Setter;
import lombok.Getter;
@Entity
@Table(name = "users")
@Setter
@Getter
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

}
