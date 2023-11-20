package com.test.api.healthcare.common.exceptions;

import java.io.Serial;

public class RetryableFailure extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1L;

    public RetryableFailure(final Throwable cause) {
        super(cause);
    }
}
