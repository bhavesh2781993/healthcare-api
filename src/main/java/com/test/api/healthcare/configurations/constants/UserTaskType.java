package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum UserTaskType {

    GENERAL,
    APPOINTMENT,
    NURSING_TASK,
    DOCTORS_NOTE,
    LAB_WORK,
    INSURANCE,
    PRIOR_AUTHORIZATION,
    TREATMENT_PLANNING,
    ON_TREATMENT,
    END_OF_TREATMENT;

    public static List<String> getAllowedValues() {
        return Arrays.stream(UserTaskType.values()).map(UserTaskType::name).toList();
    }

}
