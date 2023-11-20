package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum UserTaskStatus {

    NEW,
    PENDING,
    IN_PROGRESS,
    COMPLETE,
    BLOCKED;

    public static List<String> getAllowedValues() {
        return Arrays.stream(UserTaskStatus.values()).map(UserTaskStatus::name).toList();
    }

}
