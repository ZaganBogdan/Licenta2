package com.example.sportPlanner.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGBLOB")
    @Lob
    private byte[] photo;
    private String name;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    @OneToMany
    private List<Match> locationMatches;
}
