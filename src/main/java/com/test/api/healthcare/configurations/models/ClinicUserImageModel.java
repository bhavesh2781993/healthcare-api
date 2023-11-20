package com.test.api.healthcare.configurations.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.models.AuditableModel;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinicUserImageModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long clinicUserImageId;

    @JsonProperty(access = READ_ONLY)
    private Long clinicUserId;

    private byte[] image;

    private String imageName;

    private String imageContentType;

    private Boolean isActive;
}
