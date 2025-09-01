package org.example.iw_authentication_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class RegisterRequest {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull @NotEmpty
    private String surname;
    @NotNull
    @Past
    private LocalDate birthDate;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}