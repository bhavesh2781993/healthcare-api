package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum PositionType {
    PATIENT,
    ARM,
    LEG;

    public static List<String> getAllowedValues() {
        return Arrays.stream(PositionType.values()).map(PositionType::name).toList();
    }
}
