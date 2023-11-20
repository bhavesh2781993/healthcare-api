package com.test.api.healthcare.configurations.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.models.AuditableModel;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.UserDesignation;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ClinicUserModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long clinicUserId;

    @NotBlank(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private String name;

    @ValidateEnum(type = UserDesignation.class)
    private String designation;

    @NotNull(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private Long clinicDepartmentId;

    @NotBlank(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private String phone;

    private String alternatePhone;

    @NotBlank(message = ErrorMessage.ERR_MSG_FIELD_CAN_NOT_BE_BLANK)
    private String email;

    private String alternateEmail;

    private String address;

    private String city;

    private String stateProvince;

    private String country;

    private String postalCode;

    private ClinicUserImageModel clinicUserImage;

}

