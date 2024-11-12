package com.example.sportPlanner.dtos;


import com.example.sportPlanner.entities.RequestType;
import com.example.sportPlanner.entities.Status;
import lombok.Data;

@Data
public class RequestDto {
    private Long id;
    private Long matchId;
    private RequestType requestType;
    private String sendingUser;
    private String receivingUser;
    private Status status;
}
