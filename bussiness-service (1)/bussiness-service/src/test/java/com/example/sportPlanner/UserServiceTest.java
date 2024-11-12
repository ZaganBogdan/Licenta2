package com.example.sportPlanner;


import com.example.sportPlanner.entities.Level;
import com.example.sportPlanner.entities.Location;
import com.example.sportPlanner.entities.Match;
import com.example.sportPlanner.repositories.LocationRepository;
import com.example.sportPlanner.repositories.MatchRepository;
import com.example.sportPlanner.repositories.UserRepository;
import com.example.sportPlanner.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMatch() {
        Match match = new Match();
        match.setId(1L);
        match.setTitle("Test meci");
        match.setDate(LocalDate.parse("2024-07-15"));
        match.setStartHour(LocalTime.parse("18:00"));
        match.setEndHour(LocalTime.parse("20:00"));
        match.setLevel(Level.Amateur);
        match.setTeams(2);
        match.setPlayersPerTeam(5);
        match.setPrice(20.0);
        match.setDetails("Meci amical");

        Location location = new Location();
        location.setId(1L);
        location.setName("Test Location");

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        Match newMatch = matchService.getMatch(1L);

        assertEquals(match, newMatch);

        verify(matchRepository, times(1)).findById(1L);
    }
}
