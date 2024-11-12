package com.example.sportPlanner.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LocationDto {
    private Long id;
    @Lob
    private String photo;
    @NotEmpty
    private String name;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @NotEmpty
    private String address;

    private Double latitude;
    private Double longitude;
}
