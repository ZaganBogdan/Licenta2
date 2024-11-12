package com.example.authService.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private Type type;

    public User(Long id, String email, String username, String password, Type type) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.type = type;
    }

}
