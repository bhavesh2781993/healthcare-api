package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum WorkflowStatus {

    ACTIVE,

    INACTIVE;

    public static List<String> getAllowedValues() {
        return Arrays.stream(WorkflowStatus.values()).map(WorkflowStatus::name).toList();
    }

}
