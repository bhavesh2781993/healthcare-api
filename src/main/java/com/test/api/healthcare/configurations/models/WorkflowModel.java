package com.test.api.healthcare.configurations.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.models.AuditableModel;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.WorkflowType;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class WorkflowModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long workflowId;

    @NotBlank(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private String name;

    @ValidateEnum(type = WorkflowType.class)
    private String type;

    @NotNull(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private List<WorkflowStepModel> steps;

}
