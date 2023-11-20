package com.test.api.healthcare.configurations.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.models.AuditableModel;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.WorkflowStatus;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class ClinicWorkflowModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long clinicWorkflowId;

    private Long clinicId;

    private Long workflowId;

    @ValidateEnum(type = WorkflowStatus.class)
    private String status;

}
