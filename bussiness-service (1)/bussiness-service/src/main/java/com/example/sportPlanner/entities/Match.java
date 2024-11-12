package com.example.sportPlanner.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User owner;
    private String title;
    private LocalDate date;
    private LocalTime startHour;
    private LocalTime endHour;
    private Level level;
    private Integer teams;
    private Integer playersPerTeam;
    private Double price;
    @ManyToOne
    private Location location;
    private String details;
    private Boolean visibility;
    @JsonManagedReference
    @ManyToMany(mappedBy = "matchList")
    private List<User> players;
}
