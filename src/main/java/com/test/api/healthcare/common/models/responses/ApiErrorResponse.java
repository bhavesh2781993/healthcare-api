package com.test.api.healthcare.common.models.responses;

import com.test.api.healthcare.common.models.Error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {

    private Error error;

}
