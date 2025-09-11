package org.example.iw_authentication_service.client;

import lombok.RequiredArgsConstructor;
import org.example.iw_authentication_service.dto.RegisterRequest;
import org.example.iw_authentication_service.dto.RegisterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
    @Value("${user-service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public RegisterResponse createUser(RegisterRequest req) {
        ResponseEntity<RegisterResponse> response = restTemplate.postForEntity(
                userServiceUrl + "/api/v1.0/users",
                req,
                RegisterResponse.class
        );
        return response.getBody();
    }
    public void deleteUser(Long userId) {
        restTemplate.exchange(
                userServiceUrl + "/api/v1.0/users/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                userId
        );

    }
}