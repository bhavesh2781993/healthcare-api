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
public class PaginationModel {

    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;

}
