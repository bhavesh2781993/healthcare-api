package com.test.api.healthcare.common.exceptions;

import java.io.Serial;

public class BadConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1L;

    public BadConfigurationException(final String message) {
        super(message);
    }

}
