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
        Long UserId;
        try {
            RegisterResponse response = userServiceClient.createUser(request);
            UserId = response.getId();
        } catch (Exception e) {
            throw new UserServiceException(
                    "Failed to create user in User Service (status " +  e.getMessage() + ")"
            );
        }
        try {
            keycloakClient.createUser(request,UserId);
        } catch (Exception e) {
            try {
                userServiceClient.deleteUser(UserId);
            } catch (Exception ex) {
                throw new UserServiceException(
                        "Failed to rollback user creation in User Service ( " +  ex.getMessage() +
                                ") after Keycloak failure (" +  e.getMessage() + ")"
                );
            }
            throw new KeycloakException(
                    "Failed to create user in Keycloak (status " +  e.getMessage() + "). Rolled back successfully."
            );
        }

        return userMapper.toRegisterResponse(request, UserId);
    }

    public void delete(String email) {
        Long UserServiceId;
        try {
            UserServiceId = keycloakClient.deleteUser(email);
        } catch (Exception e) {
            throw new KeycloakException(
                    "Failed to delete user in Keycloak (status " + e.getMessage() + ")."
            );
        }
        try{
            userServiceClient.deleteUser(UserServiceId);
        }catch (Exception e){
            throw new UserServiceException(
                    "Failed to delete user in UserService (status " +  e.getMessage() + ")."
            );
        }
    }
}
