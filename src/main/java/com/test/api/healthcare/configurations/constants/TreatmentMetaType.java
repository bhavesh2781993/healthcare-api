package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;

public enum TreatmentMetaType {
    TREATMENT_SITE,
    TREATMENT_INTENT,
    TREATMENT_MODALITY,
    TREATMENT_LOCATION,
    TREATMENT_MACHINE,
    IMRT_MED_NECESSITY;

    public static List<String> getAllowedValues() {
        return Arrays.stream(TreatmentMetaType.values()).map(TreatmentMetaType::name).toList();
    }
}
