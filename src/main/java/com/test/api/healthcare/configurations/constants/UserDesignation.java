package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum UserDesignation {

    DOCTOR,
    PHYSICIAN,
    NURSE;

    public static List<String> getAllowedValues() {
        return Arrays.stream(UserDesignation.values()).map(UserDesignation::name).toList();
    }
}
