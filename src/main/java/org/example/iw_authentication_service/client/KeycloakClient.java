package org.example.iw_authentication_service.client;

import lombok.RequiredArgsConstructor;
import org.example.iw_authentication_service.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.*;
@Component
@RequiredArgsConstructor
public class KeycloakClient {

    @Value("${keycloak.server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public void createUser(RegisterRequest registerRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", registerRequest.getEmail());
        payload.put("username", registerRequest.getEmail());
        payload.put("enabled", true);
        payload.put("credentials", List.of(Map.of(
                "type", "password",
                "value", registerRequest.getPassword(),
                "temporary", false
        )));

        restTemplate.exchange(
                keycloakUrl + "/admin/realms/" + realm + "/users",
                HttpMethod.POST,
                new HttpEntity<>(payload, headers),
                Void.class
        );
    }


    private String getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");

    }

}
