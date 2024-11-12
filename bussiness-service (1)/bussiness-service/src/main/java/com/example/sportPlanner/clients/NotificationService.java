package com.example.sportPlanner.clients;

import com.example.sportPlanner.dtos.LeaveMatchDto;
import com.example.sportPlanner.dtos.NotificationDto;
import com.example.sportPlanner.entities.Request;
import com.example.sportPlanner.entities.RequestType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Value("${notificationService.Url}")
    private String urlVariable;

    public ResponseEntity<?> sendNotification(Request request) {
        return new RestTemplate().postForEntity(
                urlVariable + "/notifications/sendNotification", createNotification(request),
                String.class);
    }
    public ResponseEntity<?> sendLeaveNotification(LeaveMatchDto leaveMatchDto) {
        return new RestTemplate().postForEntity(
                urlVariable + "/notifications/sendLeaveNotification", createLeaveMatchNotification(leaveMatchDto),
                String.class);
    }

    private NotificationDto createNotification(Request request) {
        if (request.getRequestType() == RequestType.MATCH_REQUEST) {
            return createMatchNotification(request);
        }
        return createInviteNotification(request);
    }

    private NotificationDto createMatchNotification(Request request) {

        return switch (request.getStatus()) {
            case PENDING -> new NotificationDto(request.getReceivingUser().getUsername(), "User " + request.getSendingUser().getUsername() + " sent a match request.");
            case ACCEPTED -> new NotificationDto(request.getSendingUser().getUsername(), "User " + request.getReceivingUser().getUsername() + " accepted your match request.");
            case DECLINED -> new NotificationDto(request.getSendingUser().getUsername(), "User " + request.getReceivingUser().getUsername() + " declined your match request.");
            default -> new NotificationDto(request.getReceivingUser().getUsername(), "User " + request.getSendingUser().getUsername() + " cancelled the match request.");
        };

    }

    private NotificationDto createInviteNotification(Request request) {
        return switch (request.getStatus()) {
            case PENDING -> new NotificationDto(request.getReceivingUser().getUsername(), "User " + request.getSendingUser().getUsername() + " sent you an invitation.");
            case ACCEPTED -> new NotificationDto(request.getSendingUser().getUsername(), "User " + request.getReceivingUser().getUsername() + " accepted your invitation.");
            case DECLINED -> new NotificationDto(request.getSendingUser().getUsername(), "User " + request.getReceivingUser().getUsername() + " declined your invitation.");
            default -> new NotificationDto(request.getReceivingUser().getUsername(), "User " + request.getSendingUser().getUsername() + " cancelled the invitation.");

        };
    }

    private NotificationDto createLeaveMatchNotification(LeaveMatchDto leaveMatchDto){
        return new NotificationDto(leaveMatchDto.getOwnerUser(), "Sorry! User " + leaveMatchDto.getLeavingUser() + " left the match!");
    }
}
