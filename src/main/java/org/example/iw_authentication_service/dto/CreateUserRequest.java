package org.example.iw_authentication_service.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class CreateUserRequest {
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
}
