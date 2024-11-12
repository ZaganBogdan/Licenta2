package com.example.sportPlanner.service;

import com.example.sportPlanner.dtos.RequestDto;
import com.example.sportPlanner.entities.Request;
import com.example.sportPlanner.entities.RequestType;
import com.example.sportPlanner.entities.Status;
import com.example.sportPlanner.entities.User;
import com.example.sportPlanner.exceptions.MatchNotFoundException;
import com.example.sportPlanner.exceptions.RequestNotFoundException;
import com.example.sportPlanner.exceptions.UserNotFoundException;
import com.example.sportPlanner.repositories.MatchRepository;
import com.example.sportPlanner.repositories.RequestRepository;
import com.example.sportPlanner.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private MatchRepository matchRepository;

    public RequestService(RequestRepository requestRepository, UserRepository userRepository, MatchRepository matchRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    public Request addRequest(final RequestDto requestDto) {
        User sendingUser = userRepository.findUserByUsername(requestDto.getSendingUser())
                .orElseThrow(() -> new UserNotFoundException("User " + requestDto.getSendingUser() + " not found"));
        User receivingUser = userRepository.findUserByUsername(requestDto.getReceivingUser())
                .orElseThrow(() -> new UserNotFoundException("User " + requestDto.getReceivingUser() + " not found"));
        Request request = new Request();
        matchRepository.findById(requestDto.getMatchId())
                .orElseThrow(() -> new MatchNotFoundException("Sorry but we can't found this match"));
        request.setMatchId(requestDto.getMatchId());
        request.setSendingUser(sendingUser);
        request.setReceivingUser(receivingUser);
        request.setRequestType(requestDto.getRequestType());
        request.setStatus(Status.PENDING);
        requestRepository.save(request);
        return request;
    }

    public Request editRequest(final RequestDto requestDto) {
        Request request = requestRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RequestNotFoundException("Sorry, we can found this request"));
        request.setStatus(requestDto.getStatus());

        requestRepository.save(request);
        return request;
    }

    public List<Request> getMatchPendingRequests(final Long matchId) {
        return requestRepository.findRequestByMatchIdAndStatusAndRequestType(matchId, Status.PENDING, RequestType.MATCH_REQUEST)
                .orElseThrow(() -> new RequestNotFoundException("No pending requests for this match"));
    }
    public List<Request> getPendingInvitations(final Long matchId) {
        return requestRepository.findRequestByMatchIdAndStatusAndRequestType(matchId, Status.PENDING, RequestType.INVITE_REQUEST)
                .orElseThrow(() -> new RequestNotFoundException("No pending requests for this match"));
    }

}
