package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum SpreadType {
    TUMOR,
    METASTASIS,
    NODE,
    OVERALL;

    public static List<String> getAllowedValues() {
        return Arrays.stream(SpreadType.values()).map(SpreadType::name).toList();
    }
}
