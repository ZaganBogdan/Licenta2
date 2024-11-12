package com.example.sportPlanner.repositories;

import com.example.sportPlanner.entities.Match;
import com.example.sportPlanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllByPlayers(User player);

}
