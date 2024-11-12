package com.example.sportPlanner.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long matchId;
    private RequestType requestType;
    @ManyToOne
    private User sendingUser;
    @ManyToOne
    private User receivingUser;
    private Status status;

}
