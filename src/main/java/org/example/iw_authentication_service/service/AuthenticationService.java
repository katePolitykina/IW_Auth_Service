package org.example.iw_authentication_service.service;

import lombok.RequiredArgsConstructor;
import org.example.iw_authentication_service.client.KeycloakClient;
import org.example.iw_authentication_service.client.UserServiceClient;
import org.example.iw_authentication_service.dto.RegisterRequest;
import org.example.iw_authentication_service.dto.RegisterResponse;
import org.example.iw_authentication_service.exeption.KeycloakException;
import org.example.iw_authentication_service.exeption.UserServiceException;
import org.example.iw_authentication_service.mapper.UserMapper;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final KeycloakClient keycloakClient;
    private final UserServiceClient userServiceClient;
    private final UserMapper userMapper;

    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = userServiceClient.createUser(userMapper.toCreateUserRequest(request));
        Long UserId = response.getId();
        //TODO: synchronize user in keycloak and UserService
        try {
            keycloakClient.createUser(request);
        } catch (Exception e) {

            try {
                userServiceClient.deleteUser(UserId);
            } catch (UserServiceException ex) {
                throw new KeycloakException(
                        "Failed to create user in Keycloak (" + e.getMessage()+
                                ") and failed to rollback user creation in UserService: " + ex.getMessage()
                );
            }
            throw new KeycloakException(
                    "Failed to create user in Keycloak (status " +  e.getMessage() + ")"
            );
        }

        return userMapper.toRegisterResponse(request, UserId);
    }

}
