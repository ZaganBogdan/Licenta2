package com.example.sportPlanner.facade;

import com.example.sportPlanner.dtos.UserDto;
import com.example.sportPlanner.entities.User;
import com.example.sportPlanner.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }


    public User addUser(final String username) {
        return userService.addUser(username);
    }
    public User setProfile(final UserDto userDto) {
        return userService.setProfile(userDto);
    }

    public User getProfile(final String username) {
        return userService.getProfile(username);
    }

}
