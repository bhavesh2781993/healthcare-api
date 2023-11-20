package com.test.api.healthcare.common.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class InvalidFieldException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1320091007291905878L;

    private final String object;
    private final String field;

    public InvalidFieldException(final String message) {
        this("", "", message);
    }

    public InvalidFieldException(final String object, final String field, final String message) {
        super(message);
        this.field = field;
        this.object = object;
    }

}
