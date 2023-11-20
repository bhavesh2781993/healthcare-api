package com.test.api.healthcare.configurations.models;

import static com.test.api.healthcare.common.constants.ApplicationConstant.DATETIME_FORMAT;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.test.api.healthcare.common.models.AuditableModel;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.UserTaskStatus;
import com.test.api.healthcare.configurations.constants.UserTaskType;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class ClinicUserTaskModel extends AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private Long clinicUserTaskId;

    @ValidateEnum(type = UserTaskType.class)
    private String taskType;

    private Long patientId;

    private String patientName;

    private String patientMrn;

    private Long clinicId;

    @JsonFormat(pattern = DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime taskDueDate;

    private Long taskAssignedTo;

    @ValidateEnum(type = UserTaskStatus.class)
    private String taskStatus;

    private String taskNote;

    @JsonProperty(access = READ_ONLY)
    private Long taskRefId;

}
