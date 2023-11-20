package com.test.api.healthcare.common.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AuditableModel {

    @JsonProperty(access = READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = READ_ONLY)
    private LocalDateTime lastModifiedAt;

    @JsonProperty(access = READ_ONLY)
    private String createdBy;

    @JsonProperty(access = READ_ONLY)
    private String lastModifiedBy;

}
