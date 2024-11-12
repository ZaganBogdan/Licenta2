package com.example.authService.client.SportPlannerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SportPlannerService {
    @Value("${sportPlannerService.Url}")
    private String urlVariable;

    public ResponseEntity<?> addUser(String username) {
            return new RestTemplate().postForEntity(
                    urlVariable +"/user/addUser/"+ username,null,
                    String.class);
    }
}
