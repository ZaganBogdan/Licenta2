package com.example.sportPlanner.facade;

import com.example.sportPlanner.dtos.LocationDto;
import com.example.sportPlanner.entities.Location;
import com.example.sportPlanner.service.LocationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationFacade {
    private final LocationService locationService;

    public LocationFacade(LocationService locationService) {
        this.locationService = locationService;
    }

    public Location addLocation (final LocationDto locationDto){
        return locationService.addLocation(locationDto);
    }
    public List<Location> getAllLocations (){
        return locationService.getAllLocations();
    }
    public Location getLocation(final Long id) {

        return locationService.getLocation(id);
    }
}
