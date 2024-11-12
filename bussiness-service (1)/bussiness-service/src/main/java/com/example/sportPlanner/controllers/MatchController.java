package com.example.sportPlanner.controllers;

import com.example.sportPlanner.dtos.LeaveMatchDto;
import com.example.sportPlanner.dtos.MatchDto;
import com.example.sportPlanner.facade.MatchFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "home/")
public class MatchController {
    private final MatchFacade matchFacade;

    public MatchController(MatchFacade matchFacade) {
        this.matchFacade = matchFacade;
    }

    @PostMapping(value = "/addMatch")
    public ResponseEntity<?> addMatch(@RequestBody final MatchDto matchDto) {
        return ResponseEntity.ok(matchFacade.addMatch(matchDto));
    }

    @PostMapping(value = "/leaveMatch")
    public ResponseEntity<?> leaveMatch(@RequestBody final LeaveMatchDto leaveMatchDto) {
        matchFacade.leaveMatch(leaveMatchDto);
        return ResponseEntity.ok("Left match successfully");
    }

    @GetMapping(value = "/getAllMatches/{username}")
    public ResponseEntity<?> getAllMatches(@PathVariable final String username) {
        return ResponseEntity.ok(matchFacade.getAllMatches(username));
    }

    @GetMapping("/getMatch/{id}")
    public ResponseEntity<?> getMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchFacade.getMatch(id));
    }

    @PostMapping(value = "/editMatch")
    public ResponseEntity<?> editMatch(@RequestBody final MatchDto matchDto) {
        return ResponseEntity.ok(matchFacade.editMatch(matchDto));
    }

    @PostMapping("/deleteMatch/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchFacade.deleteMatch(id));
    }

    @GetMapping(value = "/getUserUpcomingMatches/{username}")
    public ResponseEntity<?> getUserUpcomingMatches(@PathVariable final String username) {
        return ResponseEntity.ok(matchFacade.getUserUpcomingMatches(username));
    }

    @GetMapping(value = "/getUserPreviousMatches/{username}")
    public ResponseEntity<?> getUserPreviousMatches(@PathVariable final String username) {
        return ResponseEntity.ok(matchFacade.getUserPreviousMatches(username));
    }

    @GetMapping(value = "/getNonParticipatingUsers/{matchId}")
    public ResponseEntity<?> getNonParticipatingUsers(@PathVariable final Long matchId){
        return ResponseEntity.ok(matchFacade.getNonParticipatingUsers(matchId));
    }

    @GetMapping(value = "/getUserInvitations/{username}")
    public ResponseEntity<?> getUserInvitations(@PathVariable final String username) {
        return ResponseEntity.ok(matchFacade.getUserInvitations(username));
    }
}
