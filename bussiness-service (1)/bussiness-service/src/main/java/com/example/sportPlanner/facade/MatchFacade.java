package com.example.sportPlanner.facade;

import com.example.sportPlanner.clients.NotificationService;
import com.example.sportPlanner.dtos.LeaveMatchDto;
import com.example.sportPlanner.dtos.MatchDto;
import com.example.sportPlanner.entities.Match;
import com.example.sportPlanner.entities.User;
import com.example.sportPlanner.service.MatchService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchFacade {
    private final MatchService matchService;
    private final NotificationService notificationService;

    public MatchFacade(MatchService matchService, NotificationService notificationService) {
        this.matchService = matchService;
        this.notificationService = notificationService;
    }

    public Match addMatch(final MatchDto matchDto){
       return matchService.addMatch(matchDto);
    }
    public Match getMatch(final Long id){
       return matchService.getMatch(id);
    }
    public List<Match> getAllMatches(final String username){
        return matchService.getAllMatches(username);
    }
    public Match editMatch(final MatchDto matchDto){
        return matchService.editMatch(matchDto);
    }
    public String deleteMatch(final Long id){
        return matchService.deleteMatch(id);
    }
    public List<Match> getUserUpcomingMatches(final String username) {
        return matchService.getUserUpcomingMatches(username);
    }
    public List<Match> getUserPreviousMatches(final String username) {
        return matchService.getUserPreviousMatches(username);
    }
    public List<Match> getUserInvitations(final String username) {
        return matchService.getUserInvitations(username);
    }
    public void leaveMatch(final LeaveMatchDto leaveMatchDto){

        matchService.leaveMatch(leaveMatchDto);
        notificationService.sendLeaveNotification(leaveMatchDto);

    }

    public List<User> getNonParticipatingUsers(final Long matchId){
        return matchService.getNonParticipatingUsers(matchId);
    }
}
