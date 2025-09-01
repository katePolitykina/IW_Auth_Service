package org.example.iw_authentication_service.mapper;

import org.example.iw_authentication_service.dto.CreateUserRequest;
import org.example.iw_authentication_service.dto.RegisterRequest;
import org.example.iw_authentication_service.dto.RegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CreateUserRequest toCreateUserRequest(RegisterRequest registerRequest);
    RegisterResponse toRegisterResponse(RegisterRequest registerRequest, Long id);
}
