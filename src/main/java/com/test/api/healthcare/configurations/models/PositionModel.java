package com.test.api.healthcare.configurations.models;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.models.AuditableModel;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.PositionType;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class PositionModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long positionId;

    @JsonProperty(access = READ_ONLY)
    private Long clinicId;

    @NotBlank(message = ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private String position;

    @ValidateEnum(type = PositionType.class)
    private String positionType;

}
