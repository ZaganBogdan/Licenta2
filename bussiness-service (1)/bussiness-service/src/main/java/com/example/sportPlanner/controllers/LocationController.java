package com.example.sportPlanner.controllers;

import com.example.sportPlanner.dtos.LocationDto;
import com.example.sportPlanner.facade.LocationFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ("/location"))
public class LocationController {

    private final LocationFacade locationFacade;


    public LocationController(LocationFacade locationFacade) {
        this.locationFacade = locationFacade;
    }

    @PostMapping("/addLocation")
    public ResponseEntity<?> addLocation(@Valid @RequestBody final LocationDto locationDto){
        return ResponseEntity.ok(locationFacade.addLocation(locationDto));
    }

    @GetMapping("/getAllLocations")
    public ResponseEntity<?> getAllLocations(){
        return ResponseEntity.ok(locationFacade.getAllLocations());
    }

    @GetMapping("/getLocation/{id}")
    public ResponseEntity<?> getLocation(@PathVariable Long id) {

        return ResponseEntity.ok(locationFacade.getLocation(id));
    }

}
