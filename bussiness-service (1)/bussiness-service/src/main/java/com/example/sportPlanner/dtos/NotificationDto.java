package com.example.sportPlanner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDto {
    private String username;
    private String message;
}
