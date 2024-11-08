package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public User(String name, String password, String email){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "username")
    private String name;
    @Column(name = "password")
    private String password;
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime localDateTime;

    @PrePersist
    protected void onCreate(){
        localDateTime = LocalDateTime.now();
    }
}
