package com.test.api.healthcare.common.utils;


import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = PRIVATE)
public class SortUtil {

    public static Sort sort(final String sortField, final String sortDirection) {
        final Sort sortBy;

        if ("ASC".equalsIgnoreCase(sortDirection)) {
            sortBy = Sort.by(sortField).ascending();
        } else {
            sortBy = Sort.by(sortField).descending();
        }

        return sortBy;
    }
}
