package com.test.api.healthcare.configurations.controllers;


import static com.test.api.healthcare.common.constants.ApplicationConstant.PAGE_SIZE;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_LOCATION_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.ClinicQueryParam;
import com.test.api.healthcare.common.models.PaginationModel;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ClinicLocationMapper;
import com.test.api.healthcare.configurations.models.ClinicLocationModel;
import com.test.api.healthcare.configurations.models.entities.ClinicLocation;
import com.test.api.healthcare.configurations.services.ClinicLocationService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/locations")
public class ClinicLocationController {

    private final ClinicLocationService clinicLocationService;
    private final ClinicLocationMapper clinicLocationMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicLocationModel>> createClinicLocation(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final ClinicLocationModel clinicLocationModel) {
        final ClinicLocation clinicLocationToCreate = clinicLocationMapper.toClinicLocation(clinicLocationModel, clinicId);
        final ClinicLocation createdClinicLocation = clinicLocationService.createClinicLocation(clinicLocationToCreate);
        final ClinicLocationModel createdClinicLocationModel = clinicLocationMapper.toClinicLocationModel(createdClinicLocation);
        final ApiResponse<ClinicLocationModel> response = ApiResponse.<ClinicLocationModel>builder()
            .data(createdClinicLocationModel).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{clinicLocationId}")
    public ResponseEntity<ApiResponse<ClinicLocationModel>> getClinicLocation(
        @PathVariable("clinicLocationId") final Long clinicLocationId) {
        final ClinicLocation matchingClinicLocation = clinicLocationService.getClinicLocation(clinicLocationId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_LOCATION_NOT_FOUND, clinicLocationId)));
        final ClinicLocationModel matchingClinicLocationModel = clinicLocationMapper.toClinicLocationModel(matchingClinicLocation);
        final ApiResponse<ClinicLocationModel> response = ApiResponse.<ClinicLocationModel>builder()
            .data(matchingClinicLocationModel).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ClinicLocationModel>> listClinicLocations(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestParam(name = "pageNo", defaultValue = "0") final int pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10")
        @Max(value = PAGE_SIZE, message = ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT) final int pageSize,
        @RequestParam(name = "sortField", defaultValue = "city") final String sortField,
        @RequestParam(name = "sortDirection", defaultValue = "ASC") final String sortDirection) {
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(clinicId)
            .pageNo(pageNo)
            .pageSize(pageSize)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .build();
        final Page<ClinicLocation> paginatedClinicLocations = clinicLocationService.listClinicLocations(clinicQueryParam);
        final List<ClinicLocationModel> paginatedClinicLocationModels =
            clinicLocationMapper.toClinicLocationModelList(paginatedClinicLocations.getContent());
        final PaginatedApiResponse<ClinicLocationModel> paginatedClinicLocationResponse =
            PaginatedApiResponse.<ClinicLocationModel>builder()
            .data(paginatedClinicLocationModels)
            .pagination(PaginationModel.builder()
                .pageNo(paginatedClinicLocations.getNumber())
                .pageSize(paginatedClinicLocations.getSize())
                .totalElements(paginatedClinicLocations.getTotalElements())
                .totalPages(paginatedClinicLocations.getTotalPages())
                .build())
            .build();
        return ResponseEntity.ok(paginatedClinicLocationResponse);
    }

    @DeleteMapping("/{clinicLocationId}")
    public ResponseEntity<Void> deleteClinicLocation(
        @PathVariable("clinicLocationId") final Long clinicLocationId) {
        clinicLocationService.deleteClinicLocation(clinicLocationId);
        return ResponseEntity.noContent().build();
    }
}
