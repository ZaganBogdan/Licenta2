package com.example.sportPlanner.controllers;


import com.example.sportPlanner.dtos.RequestDto;
import com.example.sportPlanner.facade.RequestFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/request")
public class RequestController {
    private RequestFacade requestFacade;

    public RequestController(RequestFacade requestFacade) {
        this.requestFacade = requestFacade;
    }

    @PostMapping(value = "/sendRequest")
    public ResponseEntity<?> sendRequest(@RequestBody final RequestDto requestDto) {
        return ResponseEntity.ok(requestFacade.addRequest(requestDto));
    }

    @PostMapping(value = "/responseRequest")
    public ResponseEntity<?> responseRequest(@RequestBody final RequestDto requestDto) {
        return ResponseEntity.ok(requestFacade.editRequest(requestDto));
    }

    @GetMapping(value = "/getPendingRequests/{matchId}")
    public ResponseEntity<?> getPendingRequests(@PathVariable final Long matchId) {
        return ResponseEntity.ok(requestFacade.getPendingRequests(matchId));
    }
    @GetMapping(value = "/getPendingInvitations/{matchId}")
    public ResponseEntity<?> getPendingInvitations(@PathVariable final Long matchId) {
        return ResponseEntity.ok(requestFacade.getPendingInvitations(matchId));
    }

}
