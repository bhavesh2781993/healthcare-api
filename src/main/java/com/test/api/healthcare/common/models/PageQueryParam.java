package com.test.api.healthcare.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryParam {

    private int pageNo;
    private int pageSize;
    private String sortField;
    private String sortDirection;
}
