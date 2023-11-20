package com.test.api.healthcare.common.exceptions;

public class KeycloakConflictException extends RuntimeException {

    public KeycloakConflictException(final String message) {
        super(message);
    }
}
