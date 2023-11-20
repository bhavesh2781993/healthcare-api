package com.test.api.healthcare.configurations.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.models.AuditableModel;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class  WorkflowStepModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long workflowStepId;

    @NotBlank(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private String step;

    private Integer seq;

    private Long workflowId;

}
