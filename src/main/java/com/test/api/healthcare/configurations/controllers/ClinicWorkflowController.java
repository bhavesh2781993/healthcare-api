package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.PAGE_SIZE;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_WORKFLOW_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.PageQueryParam;
import com.test.api.healthcare.common.models.PaginationModel;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ClinicWorkflowMapper;
import com.test.api.healthcare.configurations.models.ClinicWorkflowModel;
import com.test.api.healthcare.configurations.models.entities.ClinicWorkflow;
import com.test.api.healthcare.configurations.services.ClinicWorkflowService;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/clinic-workflows")
public class ClinicWorkflowController {

    private final ClinicWorkflowService clinicWorkflowService;
    private final ClinicWorkflowMapper clinicWorkflowMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicWorkflowModel>> createClinicWorkflow(
        @RequestBody @Valid final ClinicWorkflowModel clinicWorkflowModel) {
        final ClinicWorkflow clinicWorkflowToCreate = clinicWorkflowMapper.toClinicWorkflow(clinicWorkflowModel);
        final ClinicWorkflow createdClinicWorkflow = clinicWorkflowService.createClinicWorkflow(clinicWorkflowToCreate);
        final ClinicWorkflowModel createdClinicWorkflowModel = clinicWorkflowMapper.toClinicWorkflowModel(createdClinicWorkflow);
        final ApiResponse<ClinicWorkflowModel> response = ApiResponse.<ClinicWorkflowModel>builder()
            .data(createdClinicWorkflowModel).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{clinicWorkflowId}")
    public ResponseEntity<ApiResponse<ClinicWorkflowModel>> getClinicWorkflow(
        @PathVariable("clinicWorkflowId") final Long clinicWorkflowId) {
        final ClinicWorkflow matchingClinicWorkflow = clinicWorkflowService.getClinicWorkflow(clinicWorkflowId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_WORKFLOW_NOT_FOUND, clinicWorkflowId)));
        final ClinicWorkflowModel matchingClinicWorkflowModel = clinicWorkflowMapper.toClinicWorkflowModel(matchingClinicWorkflow);
        final ApiResponse<ClinicWorkflowModel> response = ApiResponse.<ClinicWorkflowModel>builder()
            .data(matchingClinicWorkflowModel).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ClinicWorkflowModel>> listClinicWorkflows(
        @RequestParam(name = "pageNo", defaultValue = "0") final int pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10")
        @Max(value = PAGE_SIZE, message = ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT) final int pageSize,
        @RequestParam(name = "sortField", defaultValue = "status") final String sortField,
        @RequestParam(name = "sortDirection", defaultValue = "ASC") final String sortDirection) {
        final PageQueryParam pageQueryParam = PageQueryParam.builder()
            .pageNo(pageNo)
            .pageSize(pageSize)
            .sortField(sortField)
            .sortDirection(sortDirection)
            .build();
        final Page<ClinicWorkflow> paginatedClinicWorkflows = clinicWorkflowService.listClinicWorkflows(pageQueryParam);
        final List<ClinicWorkflowModel> paginatedClinicWorkflowModels =
            clinicWorkflowMapper.toClinicWorkflowModelList(paginatedClinicWorkflows.getContent());
        final PaginatedApiResponse<ClinicWorkflowModel> paginatedClinicWorkflowResponse =
            PaginatedApiResponse.<ClinicWorkflowModel>builder()
                .data(paginatedClinicWorkflowModels)
                .pagination(PaginationModel.builder()
                    .pageNo(paginatedClinicWorkflows.getNumber())
                    .pageSize(paginatedClinicWorkflows.getSize())
                    .totalElements(paginatedClinicWorkflows.getTotalElements())
                    .totalPages(paginatedClinicWorkflows.getTotalPages())
                    .build())
                .build();
        return ResponseEntity.ok(paginatedClinicWorkflowResponse);
    }

    @DeleteMapping("/{clinicWorkflowId}")
    public ResponseEntity<Void> deleteClinicWorkflow(@PathVariable("clinicWorkflowId") final Long clinicWorkflowId) {
        clinicWorkflowService.deleteClinicWorkflow(clinicWorkflowId);
        return ResponseEntity.noContent().build();
    }

}


