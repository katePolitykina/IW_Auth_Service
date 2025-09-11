package org.example.iw_authentication_service.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.iw_authentication_service.dto.RegisterRequest;
import org.example.iw_authentication_service.dto.RegisterResponse;
import org.example.iw_authentication_service.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/auth")
@RequiredArgsConstructor
public class RegistrationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest request) {
         return authenticationService.register(request);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam @NotBlank String email) {
        authenticationService.delete(email);
    }
}
