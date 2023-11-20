package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum StageType {
    CLINICAL,
    PATHOLOGICAL;

    public static List<String> getAllowedValues() {
        return Arrays.stream(StageType.values()).map(StageType::name).toList();
    }
}
