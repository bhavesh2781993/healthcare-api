package com.test.api.healthcare.common.models.responses;

import com.test.api.healthcare.common.models.PaginationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedApiResponse<T> {

    private List<T> data;
    private PaginationModel pagination;

}
