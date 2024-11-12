package com.example.sportPlanner;

import com.example.sportPlanner.dtos.RequestDto;
import com.example.sportPlanner.entities.*;
import com.example.sportPlanner.repositories.MatchRepository;
import com.example.sportPlanner.repositories.RequestRepository;
import com.example.sportPlanner.repositories.UserRepository;
import com.example.sportPlanner.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RequestServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestService requestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRequestSuccess() {

        RequestDto requestDto = new RequestDto();
        requestDto.setSendingUser("sender");
        requestDto.setReceivingUser("receiver");
        requestDto.setMatchId(1L);
        requestDto.setRequestType(RequestType.MATCH_REQUEST);

        User sendingUser = new User();
        User receivingUser = new User();
        when(userRepository.findUserByUsername("sender")).thenReturn(Optional.of(sendingUser));
        when(userRepository.findUserByUsername("receiver")).thenReturn(Optional.of(receivingUser));
        when(matchRepository.findById(1L)).thenReturn(Optional.of(new Match()));

        Request savedRequest = new Request();
        when(requestRepository.save(any(Request.class))).thenReturn(savedRequest);

        Request request = requestService.addRequest(requestDto);

        assertNotNull(request);
        assertEquals(requestDto.getMatchId(), request.getMatchId());
        assertEquals(sendingUser, request.getSendingUser());
        assertEquals(receivingUser, request.getReceivingUser());
        assertEquals(requestDto.getRequestType(), request.getRequestType());
        assertEquals(Status.PENDING, request.getStatus());

        verify(userRepository).findUserByUsername("sender");
        verify(userRepository).findUserByUsername("receiver");
        verify(matchRepository).findById(1L);
        verify(requestRepository).save(any(Request.class));
    }
}
