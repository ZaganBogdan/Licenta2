package com.example.sportPlanner.repositories;

import com.example.sportPlanner.entities.Request;
import com.example.sportPlanner.entities.RequestType;
import com.example.sportPlanner.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
       Optional <List<Request>> findRequestByMatchIdAndStatusAndRequestType(Long matchId, Status status, RequestType requestType);
       Optional <List<Request>> findRequestByMatchIdAndStatus(Long matchId, Status status);
       Optional <List<Request>> findRequestByReceivingUserIdAndStatusAndRequestType(Long id, Status status, RequestType requestType);
}
