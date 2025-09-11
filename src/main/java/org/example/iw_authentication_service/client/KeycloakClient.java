package org.example.iw_authentication_service.client;

import lombok.RequiredArgsConstructor;
import org.example.iw_authentication_service.dto.RegisterRequest;
import org.example.iw_authentication_service.exeption.KeycloakException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;
    public void createUser(RegisterRequest registerRequest, Long userId) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerRequest.getEmail()); // email = username
        user.setEmail(registerRequest.getEmail());
        user.setEnabled(true);

        user.setAttributes(Map.of("userServiceId", List.of(String.valueOf(userId))));

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        keycloak.realm(realm).users().create(user);
    }

    public Long deleteUser(String email) {
        List<UserRepresentation> users = keycloak.realm(realm).users().search(email, true);
        if (users.isEmpty()) {
            throw new KeycloakException("User with email " + email + " not found");
        }

        UserRepresentation user = users.get(0);

        Long userServiceId = null;
        if (user.getAttributes() != null && user.getAttributes().containsKey("userServiceId")) {
            List<String> ids = user.getAttributes().get("userServiceId");
            if (ids != null && !ids.isEmpty()) {
                userServiceId = Long.valueOf(ids.get(0));
            }
        }

        keycloak.realm(realm).users().get(user.getId()).remove();

        return userServiceId;
    }

}
