package com.example.sportPlanner.dtos;

import lombok.Data;

@Data
public class LeaveMatchDto {
    private Long matchId;
    private String leavingUser;
    private String ownerUser;
}
