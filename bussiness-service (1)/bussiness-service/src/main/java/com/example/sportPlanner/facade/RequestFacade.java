package com.example.sportPlanner.facade;

import com.example.sportPlanner.clients.NotificationService;
import com.example.sportPlanner.dtos.RequestDto;
import com.example.sportPlanner.entities.Request;
import com.example.sportPlanner.entities.Status;
import com.example.sportPlanner.service.MatchService;
import com.example.sportPlanner.service.RequestService;
import com.example.sportPlanner.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestFacade {
    private RequestService requestService;
    private MatchService matchService;
    private UserService userService;
    private final NotificationService notificationService;

    public RequestFacade(RequestService requestService, MatchService matchService, UserService userService, NotificationService notificationService) {
        this.requestService = requestService;
        this.matchService = matchService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Request
    addRequest(final RequestDto requestDto) {
        Request request = requestService.addRequest(requestDto);
        notificationService.sendNotification(request);
        return request;
    }

    public Request editRequest(final RequestDto requestDto) {
        Request request = requestService.editRequest(requestDto);
        if (request.getStatus().equals(Status.ACCEPTED)) {
            matchService.addPlayerToMatch(request);
            userService.addMatchToPlayer(request);

        }
        notificationService.sendNotification(request);
        return request;
    }

    public List<Request> getPendingRequests(final Long matchId) {
        return requestService.getMatchPendingRequests(matchId);
    }
    public List<Request> getPendingInvitations(final Long matchId) {
        return requestService.getPendingInvitations(matchId);
    }
}
