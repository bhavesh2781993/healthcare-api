package com.test.api.healthcare.configurations.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.models.AuditableModel;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.SpreadType;
import com.test.api.healthcare.configurations.constants.StageType;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class TreatmentStageModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long treatmentStageId;

    @JsonProperty(access = READ_ONLY)
    private Long clinicId;

    @ValidateEnum(type = StageType.class)
    private String stageType;

    @ValidateEnum(type = SpreadType.class)
    private String spreadType;

    @NotBlank(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private String stage;
}
