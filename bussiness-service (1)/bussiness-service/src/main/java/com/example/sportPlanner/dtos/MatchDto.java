package com.example.sportPlanner.dtos;

import com.example.sportPlanner.entities.Level;
import lombok.Data;

@Data
public class MatchDto {
    private Long id;
    private String title;
    private String owner;
    private String date;
    private String startHour;
    private String endHour;
    private Level level;
    private String teams;
    private String playersPerTeam;
    private String price;
    private Long locationId;
    private String details;
    private Boolean visibility;
}
