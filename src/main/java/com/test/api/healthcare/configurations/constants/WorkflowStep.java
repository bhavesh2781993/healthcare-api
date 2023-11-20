package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum WorkflowStep {

    PRE_TREATMENT,
    TREATMENT_PLANNING,
    ON_TREATMENT,
    END_OF_TREATMENT,
    CHART;

    public static List<String> getAllowedValues() {
        return Arrays.stream(WorkflowStep.values()).map(WorkflowStep::name).toList();
    }
}
