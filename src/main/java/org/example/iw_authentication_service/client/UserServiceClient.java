package org.example.iw_authentication_service.client;

import lombok.RequiredArgsConstructor;
import org.example.iw_authentication_service.dto.CreateUserRequest;
import org.example.iw_authentication_service.dto.RegisterResponse;
import org.example.iw_authentication_service.exeption.UserServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
    @Value("${user-service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public RegisterResponse createUser(CreateUserRequest req) {
        try {
            ResponseEntity<RegisterResponse> response = restTemplate.postForEntity(
                    userServiceUrl + "/api/v1.0/users",
                    req,
                    RegisterResponse.class
            );
            return response.getBody();

        }catch (HttpClientErrorException | HttpServerErrorException ex) {
            String errorBody = ex.getResponseBodyAsString();
            throw new UserServiceException("UserService creation failed: "+ ex.getStatusCode().value() +" "+ errorBody);
        }
    }
    public void deleteUser(Long userId) {
        try {
            restTemplate.exchange(
                    userServiceUrl + "/api/v1.0/users/{id}",
                    HttpMethod.DELETE,
                    null,
                    Void.class,
                    userId
            );
        }catch (HttpClientErrorException | HttpServerErrorException ex) {
            String errorBody = ex.getResponseBodyAsString();
            throw new UserServiceException("UserService creation failed: "+ ex.getStatusCode().value() +" "+ errorBody);
        }
    }
}