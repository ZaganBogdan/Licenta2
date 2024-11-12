package com.example.sportPlanner;

import com.example.sportPlanner.dtos.MatchDto;
import com.example.sportPlanner.entities.Level;
import com.example.sportPlanner.entities.Location;
import com.example.sportPlanner.entities.Match;
import com.example.sportPlanner.entities.User;
import com.example.sportPlanner.exceptions.MatchNotFoundException;
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
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MatchServiceTest {

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
    public void testAddMatch() {
        MatchDto matchDto = new MatchDto();
        matchDto.setTitle("Test meci");
        matchDto.setOwner("a");
        matchDto.setDate("2024-07-15");
        matchDto.setStartHour("18:00");
        matchDto.setEndHour("20:00");
        matchDto.setLevel(Level.Amateur);
        matchDto.setTeams("2");
        matchDto.setPlayersPerTeam("5");
        matchDto.setPrice("20");
        matchDto.setLocationId(1L);
        matchDto.setDetails("Meci amical");

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

        User owner = new User();
        owner.setUsername("a");
        owner.setId(1L);
        owner.setMatchList(new ArrayList<>());

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(userRepository.findUserByUsername("a")).thenReturn(Optional.of(owner));
        when(matchRepository.save(any(Match.class))).thenReturn(match);


        Match newMatch = matchService.addMatch(matchDto);

        assertNotNull(newMatch);

        verify(locationRepository, times(1)).findById(1L);
        verify(userRepository, times(2)).findUserByUsername("a");
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    public void testEditMatch() {
        MatchDto matchDto = new MatchDto();
        matchDto.setId(1L);
        matchDto.setTitle("Test meci");
        matchDto.setDate("2024-07-15");
        matchDto.setStartHour("18:00");
        matchDto.setEndHour("20:00");
        matchDto.setLevel(Level.Amateur);
        matchDto.setTeams("2");
        matchDto.setPlayersPerTeam("5");
        matchDto.setPrice("20");
        matchDto.setLocationId(1L);
        matchDto.setDetails("Meci amical");

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
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match newMatch = matchService.editMatch(matchDto);

        assertNotNull(newMatch);

        verify(locationRepository, times(1)).findById(1L);
        verify(userRepository, times(2)).findUserByUsername("a");
        verify(matchRepository, times(1)).save(any(Match.class));
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

    @Test
    public void testGetMatchNotFoundException() {
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        MatchNotFoundException exception = assertThrows(
                MatchNotFoundException.class,
                () -> matchService.getMatch(1L)
        );

        assertEquals(MatchNotFoundException.class, exception.getClass());

        verify(matchRepository, times(1)).findById(1L);
    }
}
