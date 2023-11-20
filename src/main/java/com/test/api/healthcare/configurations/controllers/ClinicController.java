package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.PAGE_SIZE;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.PageQueryParam;
import com.test.api.healthcare.common.models.PaginationModel;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ClinicMapper;
import com.test.api.healthcare.configurations.models.ClinicModel;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.services.ClinicService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.constraints.Max;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/clinics")
public class ClinicController {

    private final ClinicMapper clinicMapper;
    private final ClinicService clinicService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicModel>> createClinic(@RequestBody final ClinicModel clinicModel) {
        final Clinic clinicToCreate = clinicMapper.clinicModelToClinic(clinicModel);
        final Clinic createdClinic = clinicService.createClinic(clinicToCreate);
        final ClinicModel createdClinicModel = clinicMapper.clinicToClinicModel(createdClinic);
        final ApiResponse<ClinicModel> response = ApiResponse.<ClinicModel>builder()
            .data(createdClinicModel).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{clinicId}")
    @PreAuthorize("@roleAuthorityEvaluator.hasAuthority('DELETE')")
    public ResponseEntity<ApiResponse<ClinicModel>> getClinic(@PathVariable("clinicId") final Long clinicId) {
        final Clinic matchingClinic = clinicService.getClinic(clinicId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_NOT_FOUND, clinicId)));
        final ClinicModel matchingClinicModel = clinicMapper.clinicToClinicModel(matchingClinic);

        final ApiResponse<ClinicModel> response = ApiResponse.<ClinicModel>builder()
            .data(matchingClinicModel).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("@roleAuthorityEvaluator.hasAuthority('DELETE')")
    public ResponseEntity<PaginatedApiResponse<ClinicModel>> listClinics(
        @RequestParam(name = "pageNo", defaultValue = "0") final int pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10")
        @Max(value = PAGE_SIZE, message = ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT) final int pageSize,
        @RequestParam(name = "sortField", defaultValue = "departmentName") final String sortField,
        @RequestParam(name = "sortDirection", defaultValue = "ASC") final String sortDirection)  {
        final PageQueryParam pageQueryParam = PageQueryParam.builder()
            .pageNo(pageNo)
            .pageSize(pageSize)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .build();
        final Page<Clinic> paginatedClinics = clinicService.listClinics(pageQueryParam);
        final List<ClinicModel> paginatedClinicModels = clinicMapper.toClinicModelList(paginatedClinics.getContent());
        final PaginatedApiResponse<ClinicModel> paginatedClinicResponse = PaginatedApiResponse.<ClinicModel>builder()
            .data(paginatedClinicModels)
            .pagination(PaginationModel.builder()
                .pageNo(paginatedClinics.getNumber())
                .pageSize(paginatedClinics.getSize())
                .totalElements(paginatedClinics.getTotalElements())
                .totalPages(paginatedClinics.getTotalPages())
                .build())
            .build();
        return ResponseEntity.ok(paginatedClinicResponse);
    }

    @DeleteMapping("/{clinicId}")
    public ResponseEntity<Void> deleteClinic(@PathVariable("clinicId") final Long clinicId) {
        clinicService.deleteClinic(clinicId);
        return ResponseEntity.ok().build();
    }

}
