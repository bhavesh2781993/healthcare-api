package com.test.api.healthcare.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStepQueryParam {

    private int pageNo;
    private int pageSize;
    private String filter;
    private String sort;
    private String step;

}
