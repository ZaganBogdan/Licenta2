package com.example.sportPlanner.service;

import com.example.sportPlanner.dtos.UserDto;
import com.example.sportPlanner.entities.Match;
import com.example.sportPlanner.entities.Request;
import com.example.sportPlanner.entities.RequestType;
import com.example.sportPlanner.entities.User;
import com.example.sportPlanner.exceptions.*;
import com.example.sportPlanner.repositories.MatchRepository;
import com.example.sportPlanner.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    public UserService(UserRepository userRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    public User addUser(final String username) {
        User user = new User();
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username " + username + " is already used!");
        }
        user.setUsername(username);
        return userRepository.save(user);
    }

    public User setProfile(final UserDto userDto) {
        User user = userRepository.findUserByUsername(userDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Username " + userDto.getUsername() + " not found!"));
        validations(userDto);
        user.setId(userDto.getId());
        if(userDto.getPhoto() != null) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(userDto.getPhoto()));
            if (fileName.contains("..")) {
                System.out.println("not a a valid file");
            }

            File file = new File(userDto.getPhoto());
            if (file.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream(file);) {
                    user.setPhoto(fileInputStream.readAllBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        if (userDto.getBirthday() != null) {
            user.setBirthday(LocalDate.parse(userDto.getBirthday()));
        }
        user.setCountry(userDto.getCountry());
        user.setLocation(userDto.getLocation());
        user.setNationality(userDto.getNationality());
        if (userDto.getAge() != null) {
            user.setAge(Integer.parseInt(userDto.getAge()));
        }
        if (userDto.getHeight() != null) {
            user.setHeight(Integer.parseInt(userDto.getHeight()));
        }
        if (userDto.getWeight() != null) {
            user.setWeight(Integer.parseInt(userDto.getWeight()));
        }

        user.setPosition(userDto.getPosition());
        user.setFoot(userDto.getFoot());
        return userRepository.save(user);
    }

    public User getProfile(final String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Sorry, but we can't find profile for " + username + "."));

    }

    public User addMatchToPlayer(final Request request) {
        User user;
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new MatchNotFoundException("Soryy, we can't find your match."));
        if (request.getRequestType().equals(RequestType.MATCH_REQUEST)) {
            user = userRepository.findUserByUsername(request.getSendingUser().getUsername())
                    .orElseThrow(() -> new UserNotFoundException("Username " + request.getSendingUser() + " not found!"));
        } else {
            user = userRepository.findUserByUsername(request.getReceivingUser().getUsername())
                    .orElseThrow(() -> new UserNotFoundException("Username " + request.getReceivingUser() + " not found!"));
        }
        if (user.getMatchList().contains(match)) {
            throw new MatchAlreadyAddedToUserException("Sorry, but this match is already on match list");
        }
        user.getMatchList().add(match);
        return userRepository.save(user);

    }

    private void validations(final UserDto userDto) {
        final List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp");
//        if (allowedMimeTypes.contains(userDto.getPhoto().getContentType())) {
//            throw new InvalidImageTypeException("Only image files are allowed");
//        }

        if (!userDto.getPhone().isEmpty() && userDto.getPhone().length() < 9) {
            throw new IncorrectInputException("Please insert a valid number!");
        }
    }
}
