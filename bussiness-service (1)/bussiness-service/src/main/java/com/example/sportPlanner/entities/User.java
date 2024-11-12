package com.example.sportPlanner.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGBLOB")
    @Lob
    private byte[] photo;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate birthday;
    private String country;
    private String location;
    private String nationality;
    private Integer age;
    private Integer height;
    private Integer weight;
    private Position position;
    private Foot foot;
    @ManyToMany
    @JsonBackReference
    @JoinTable(
            name = "user_matches",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    private List<Match> matchList;

}
