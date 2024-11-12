package com.example.sportPlanner.service;

import com.example.sportPlanner.dtos.LeaveMatchDto;
import com.example.sportPlanner.dtos.MatchDto;
import com.example.sportPlanner.entities.*;
import com.example.sportPlanner.exceptions.*;
import com.example.sportPlanner.repositories.LocationRepository;
import com.example.sportPlanner.repositories.MatchRepository;
import com.example.sportPlanner.repositories.RequestRepository;
import com.example.sportPlanner.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;

    public MatchService(MatchRepository matchRepository, UserRepository userRepository, RequestRepository requestRepository, LocationRepository locationRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.locationRepository = locationRepository;
    }

    public Match addMatch(final MatchDto matchDto) {
        validateMatchValues(matchDto);
        Match match = new Match();
        match.setTitle(matchDto.getTitle());
        match.setDate(LocalDate.parse(matchDto.getDate()));
        match.setStartHour(LocalTime.parse(matchDto.getStartHour()));
        match.setEndHour(LocalTime.parse(matchDto.getEndHour()));
        match.setLevel(matchDto.getLevel());
        match.setTeams(Integer.valueOf(matchDto.getTeams()));
        match.setPlayersPerTeam(Integer.valueOf(matchDto.getPlayersPerTeam()));
        match.setPrice(Double.valueOf(matchDto.getPrice()));
        match.setDetails(matchDto.getDetails());
        match.setVisibility(matchDto.getVisibility());
        Location location = locationRepository.findById(matchDto.getLocationId())
                        .orElseThrow(() -> new LocationNotFoundException("Sorry, location not found"));
        match.setLocation(location);
        User owner = userRepository.findUserByUsername(matchDto.getOwner())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        match.setOwner(owner);
        owner.getMatchList().add(match);

        return matchRepository.save(match);
    }

    public Match editMatch(final MatchDto matchDto) {
        validateMatchValues(matchDto);
        Match match = matchRepository.findById(matchDto.getId())
                .orElseThrow(() -> new MatchNotFoundException("Soryy, we can't find your match."));
        match.setId(matchDto.getId());
        match.setTitle(matchDto.getTitle());
        match.setDate(LocalDate.parse(matchDto.getDate()));
        match.setStartHour(LocalTime.parse(matchDto.getStartHour()));
        match.setEndHour(LocalTime.parse(matchDto.getEndHour()));
        match.setLevel(matchDto.getLevel());
        match.setTeams(Integer.valueOf(matchDto.getTeams()));
        match.setPlayersPerTeam(Integer.valueOf(matchDto.getPlayersPerTeam()));
        match.setPrice(Double.valueOf(matchDto.getPrice()));
        match.setDetails(matchDto.getDetails());
        match.setVisibility(matchDto.getVisibility());
        Location location = locationRepository.findById(matchDto.getId())
                .orElseThrow(() -> new LocationNotFoundException("Sorry, location not found"));
        match.setLocation(location);

        return matchRepository.save(match);
    }

    public Match getMatch(final Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException("Soryy, we can't find your match."));
    }

    public String deleteMatch(final Long id) {
        matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException("Soryy, we can't find your match."));
        matchRepository.deleteById(id);
        return "Match deleted successfully!";
    }

    public List<Match> getAllMatches(final String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("Sorry, we can't find this user"));
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Match> upcomingMatches = matchRepository.findAll()
                .stream()
                .filter(match -> match.getDate().isAfter(currentDate) ||
                        (match.getDate().isEqual(currentDate) && match.getStartHour().isAfter(currentTime)))
                .collect(Collectors.toList());

        return upcomingMatches.stream()
                .filter(match -> {
                    int totalPlayers = match.getPlayers().size();
                    int maxPlayers = match.getTeams() * match.getPlayersPerTeam();
                    return totalPlayers < maxPlayers;
                })
                .filter(match -> !match.getPlayers().contains(user))
                .collect(Collectors.toList());
    }

    public Match addPlayerToMatch(final Request request) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new MatchNotFoundException("Soryy, we can't find your match."));

        if(request.getRequestType().equals(RequestType.MATCH_REQUEST)) {
            if (match.getPlayers().contains(request.getSendingUser())) {
                throw new UserAlreadyAddedToMatchException("Sorry, but this user is already on players list");
            }
            match.getPlayers().add(request.getSendingUser());

        }else {
            if (match.getPlayers().contains(request.getReceivingUser())) {
                throw new UserAlreadyAddedToMatchException("Sorry, but this user is already on players list");
            }
            match.getPlayers().add(request.getReceivingUser());
        }
        return matchRepository.save(match);
    }

    public List<Match> getUserUpcomingMatches(final String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("Sorry, we can't find this user"));
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        return matchRepository.findAllByPlayers(user).stream()
                .filter(match -> match.getDate().isAfter(currentDate) ||
                        (match.getDate().isEqual(currentDate) && match.getStartHour().isAfter(currentTime)))
                .collect(Collectors.toList());

    }

    public List<Match> getUserPreviousMatches(final String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("Sorry, we can't find this user"));
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Match> matches = matchRepository.findAllByPlayers(user);
        return matches.stream()
                .filter(match -> match.getDate().isBefore(currentDate) ||
                        (match.getDate().isEqual(currentDate) && match.getStartHour().isBefore(currentTime)))
                .collect(Collectors.toList());

    }

    public void leaveMatch(final LeaveMatchDto leaveMatchDto) {
        User user = userRepository.findUserByUsername(leaveMatchDto.getLeavingUser())
                .orElseThrow(() -> new UserNotFoundException("Sorry, we can't find this user"));
        Match match = matchRepository.findById(leaveMatchDto.getMatchId())
                .orElseThrow(() -> new MatchNotFoundException("Soryy, we can't find your match."));
        match.getPlayers().remove(user);
        user.getMatchList().remove(match);
        userRepository.save(user);
        matchRepository.save(match);
    }

    public List<User> getNonParticipatingUsers(final Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Soryy, we can't find your match."));
        List<Request> pendingRequests = requestRepository.findRequestByMatchIdAndStatus(matchId, Status.PENDING)
                .orElseThrow(() -> new RequestNotFoundException("Sorry, we can found this request"));
        List<User> users = userRepository.findAll();
        users = users.stream()
                .filter(user -> !match.getPlayers().contains(user))
                .filter(user -> pendingRequests.stream()
                        .noneMatch(request -> request.getReceivingUser().equals(user)))
                .collect(Collectors.toList());
        return users;

    }

    private void validateMatchValues(final MatchDto matchDto) {
        if (LocalDate.parse(matchDto.getDate()).isBefore(LocalDate.now())) {
            throw new IncorrectDateException("Please select a valid date!");
        }
        if (LocalTime.parse(matchDto.getStartHour()).isAfter(LocalTime.parse(matchDto.getEndHour())) &&
                LocalTime.parse(matchDto.getStartHour()).isAfter(LocalTime.now())) {
            throw new IncorrectDateException("Please select valid hours.");
        }
        if (!isNumeric(matchDto.getPrice())) {
            throw new InvalidPriceException("Please introduce a valid price");
        }
        if (!userRepository.findUserByUsername(matchDto.getOwner()).isPresent()) {
            throw new UserNotFoundException("Sorry, user " + matchDto.getOwner() + " not found.");
        }
    }

    public List<Match> getUserInvitations(final String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Sorry, we can't find this user"));

        List<Request> pendingRequests = requestRepository.findRequestByReceivingUserIdAndStatusAndRequestType(user.getId(),Status.PENDING, RequestType.INVITE_REQUEST)
                .orElseThrow(() -> new RequestNotFoundException("Sorry, we can't find any pending invite requests"));

        List<Match> matches = new ArrayList<>();
        pendingRequests.forEach(request -> {
            matchRepository.findById(request.getMatchId())
                    .ifPresent(matches::add);
        });

        return matches;
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
