package org.example.iw_authentication_service.exeption;

public class KeycloakException extends RuntimeException {
    public KeycloakException(String message) { super(message); }
}
