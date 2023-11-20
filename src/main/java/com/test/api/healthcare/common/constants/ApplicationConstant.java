package com.test.api.healthcare.common.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ApplicationConstant {

    public static final String REQUEST_HEADER_CLINIC_ID = "x-no-clinic-id";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final long PAGE_SIZE = 100L;

}
