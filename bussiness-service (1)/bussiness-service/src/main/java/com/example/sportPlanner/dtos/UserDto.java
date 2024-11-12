package com.example.sportPlanner.dtos;

import com.example.sportPlanner.entities.Foot;
import com.example.sportPlanner.entities.Position;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    @Lob
    private String  photo;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String birthday;
    private String country;
    private String location;
    private String nationality;
    private String age;
    private String height;
    private String weight;
    private Position position;
    private Foot foot;
}
