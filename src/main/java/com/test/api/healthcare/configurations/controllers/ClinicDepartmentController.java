package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.PAGE_SIZE;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_DEPARTMENT_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.ClinicQueryParam;
import com.test.api.healthcare.common.models.PaginationModel;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ClinicDepartmentMapper;
import com.test.api.healthcare.configurations.models.ClinicDepartmentModel;
import com.test.api.healthcare.configurations.models.entities.ClinicDepartment;
import com.test.api.healthcare.configurations.services.ClinicDepartmentService;

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
@RequestMapping("/departments")
public class ClinicDepartmentController {

    private final ClinicDepartmentService clinicDepartmentService;
    private final ClinicDepartmentMapper clinicDepartmentMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicDepartmentModel>> createClinicDepartment(
        @RequestBody @Valid final ClinicDepartmentModel clinicDepartmentModel) {
        final ClinicDepartment clinicDepartmentToCreate = clinicDepartmentMapper.toClinicDepartment(clinicDepartmentModel);
        final ClinicDepartment createdClinicDepartment = clinicDepartmentService.createClinicDepartment(clinicDepartmentToCreate);
        final ClinicDepartmentModel createdClinicDepartmentModel = clinicDepartmentMapper.toClinicDepartmentModel(createdClinicDepartment);
        final ApiResponse<ClinicDepartmentModel> response = ApiResponse.<ClinicDepartmentModel>builder()
            .data(createdClinicDepartmentModel).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{clinicDepartmentId}")
    public ResponseEntity<ApiResponse<ClinicDepartmentModel>> getClinicDepartment(
        @PathVariable("clinicDepartmentId") final Long clinicDepartmentId) {
        final ClinicDepartment matchingClinicDepartment = clinicDepartmentService.getClinicDepartment(clinicDepartmentId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_DEPARTMENT_NOT_FOUND, clinicDepartmentId)));
        final ClinicDepartmentModel matchingClinicDepartmentModel =
            clinicDepartmentMapper.toClinicDepartmentModel(matchingClinicDepartment);
        final ApiResponse<ClinicDepartmentModel> response = ApiResponse.<ClinicDepartmentModel>builder()
            .data(matchingClinicDepartmentModel).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ClinicDepartmentModel>> listClinicDepartments(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestParam(name = "pageNo", defaultValue = "0") final int pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10")
        @Max(value = PAGE_SIZE, message = ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT) final int pageSize,
        @RequestParam(name = "sortField", defaultValue = "departmentName") final String sortField,
        @RequestParam(name = "sortDirection", defaultValue = "ASC") final String sortDirection) {
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(clinicId)
            .pageNo(pageNo)
            .pageSize(pageSize)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .build();
        final Page<ClinicDepartment> clinicDepartmentList = clinicDepartmentService.listClinicDepartments(clinicQueryParam);
        final List<ClinicDepartmentModel> clinicDepartmentModels =
            clinicDepartmentMapper.toClinicDepartmentModelList(clinicDepartmentList.getContent());
        final PaginatedApiResponse<ClinicDepartmentModel> paginatedClinicDepartmentResponse =
            PaginatedApiResponse.<ClinicDepartmentModel>builder()
                .data(clinicDepartmentModels).pagination(PaginationModel.builder()
                .pageNo(clinicDepartmentList.getNumber())
                .pageSize(clinicDepartmentList.getSize())
                .totalElements(clinicDepartmentList.getTotalElements())
                .totalPages(clinicDepartmentList.getTotalPages())
                .build())
                .build();
        return ResponseEntity.ok(paginatedClinicDepartmentResponse);
    }

    @DeleteMapping("/{clinicDepartmentId}")
    public ResponseEntity<Void> deleteClinicDepartment(@PathVariable("clinicDepartmentId") final Long clinicDepartmentId) {
        clinicDepartmentService.deleteClinicDepartment(clinicDepartmentId);
        return ResponseEntity.noContent().build();
    }
}
