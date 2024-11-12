package com.example.sportPlanner.service;

import com.example.sportPlanner.dtos.LocationDto;
import com.example.sportPlanner.entities.Location;
import com.example.sportPlanner.exceptions.LocationNotFoundException;
import com.example.sportPlanner.repositories.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class LocationService {
    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location addLocation(final LocationDto locationDto){
        Location location = new Location();
        location.setName(locationDto.getName());
        location.setAddress(locationDto.getAddress());
        location.setDescription(locationDto.getDescription());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(locationDto.getPhoto()));
        if(fileName.contains(".."))
        {
            System.out.println("not a a valid file");
        }

        File file =  new File(locationDto.getPhoto());
        if(file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);) {
                location.setPhoto(fileInputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }
    public Location getLocation(final Long id) {

        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Soryy, we can't find this location."));
    }
}
