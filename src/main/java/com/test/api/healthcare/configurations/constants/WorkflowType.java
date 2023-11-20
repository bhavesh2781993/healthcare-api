package com.test.api.healthcare.configurations.constants;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum WorkflowType {

    REGULAR,
    BOOST_WITH_SIM,
    BOOST_WITHOUT_SIM,
    ADAPTATION_WITH_SIM,
    ADAPTATION_WITHOUT_SIM;

    public static final List<WorkflowType> BOOST_ADAPTATION_WF_TYPES =
        List.of(BOOST_WITH_SIM, BOOST_WITHOUT_SIM, ADAPTATION_WITH_SIM, ADAPTATION_WITHOUT_SIM);

    public static final Predicate<WorkflowType> IS_WF_TYPE_BOOST_OR_ADAPTATION = BOOST_ADAPTATION_WF_TYPES::contains;

    public static List<String> getAllowedValues() {
        return Arrays.stream(WorkflowType.values()).map(WorkflowType::name).toList();
    }
}

