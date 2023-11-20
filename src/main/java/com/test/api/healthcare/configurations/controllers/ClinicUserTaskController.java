package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.PAGE_SIZE;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_USER_TASK_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.FiqlQueryParam;
import com.test.api.healthcare.common.models.PaginationModel;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ClinicUserTaskMapper;
import com.test.api.healthcare.configurations.models.ClinicUserTaskModel;
import com.test.api.healthcare.configurations.models.entities.ClinicUserTask;
import com.test.api.healthcare.configurations.services.ClinicUserTaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/clinic-user-tasks")
public class ClinicUserTaskController {

    private final ClinicUserTaskService clinicUserTaskService;
    private final ClinicUserTaskMapper clinicUserTaskMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicUserTaskModel>> createClinicUserTask(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final ClinicUserTaskModel clinicUserTaskModel) {
        final ClinicUserTask clinicUserTask = clinicUserTaskMapper.toClinicUserTask(clinicUserTaskModel, clinicId);

        final ClinicUserTask createdClinicUserTask = clinicUserTaskService.createClinicUserTask(clinicUserTask);

        final ClinicUserTaskModel createdClinicUserTaskModel = clinicUserTaskMapper.toClinicUserTaskModel(createdClinicUserTask);
        final ApiResponse<ClinicUserTaskModel> response = ApiResponse.<ClinicUserTaskModel>builder()
            .data(createdClinicUserTaskModel).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{clinicUserTaskId}")
    public ResponseEntity<ApiResponse<ClinicUserTaskModel>> updateClinicUserTask(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("clinicUserTaskId") final Long clinicUserTaskId,
        @RequestBody @Valid final ClinicUserTaskModel clinicUserTaskModel) {
        final ClinicUserTask clinicUserTask = clinicUserTaskMapper.toClinicUserTask(clinicUserTaskModel, clinicUserTaskId, clinicId);

        final ClinicUserTask updatedClinicUserTask = clinicUserTaskService.updateClinicUserTask(clinicUserTask);
        final ClinicUserTaskModel createdClinicUserTaskModel = clinicUserTaskMapper.toClinicUserTaskModel(updatedClinicUserTask);
        final ApiResponse<ClinicUserTaskModel> response = ApiResponse.<ClinicUserTaskModel>builder()
            .data(createdClinicUserTaskModel).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clinicUserTaskId}")
    public ResponseEntity<ApiResponse<ClinicUserTaskModel>> getClinicUserTask(
        @PathVariable("clinicUserTaskId") final Long clinicUserTaskId) {
        final ClinicUserTask matchingClinicUserTask = clinicUserTaskService.getClinicUserTask(clinicUserTaskId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_USER_TASK_NOT_FOUND, clinicUserTaskId)));

        final ClinicUserTaskModel matchingClinicUserTaskModel = clinicUserTaskMapper.toClinicUserTaskModel(matchingClinicUserTask);
        final ApiResponse<ClinicUserTaskModel> response = ApiResponse.<ClinicUserTaskModel>builder()
            .data(matchingClinicUserTaskModel).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ClinicUserTaskModel>> listClinicUserTasks(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestParam(name = "filter", required = false) final String filter,
        @RequestParam(name = "sort", required = false) final String sort,
        @RequestParam(name = "pageNo", defaultValue = "0") final int pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10")
        @Max(value = PAGE_SIZE, message = ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT) final int pageSize) {
        final String modifiedFilter = appendClinicIdToFilter(filter, clinicId);
        final FiqlQueryParam queryParam = FiqlQueryParam.builder()
            .filter(modifiedFilter)
            .sort(sort)
            .pageNo(pageNo)
            .pageSize(pageSize)
            .build();

        final Page<ClinicUserTask> clinicUserTaskPage = clinicUserTaskService.listClinicUserTasks(queryParam);

        final List<ClinicUserTaskModel> clinicUserTaskModelList =
            clinicUserTaskMapper.toClinicUserTaskModelList(clinicUserTaskPage.getContent());
        final PaginatedApiResponse<ClinicUserTaskModel> response = PaginatedApiResponse.<ClinicUserTaskModel>builder()
            .data(clinicUserTaskModelList)
            .pagination(PaginationModel.builder()
                .pageNo(clinicUserTaskPage.getNumber())
                .pageSize(clinicUserTaskPage.getSize())
                .totalPages(clinicUserTaskPage.getTotalPages())
                .totalElements(clinicUserTaskPage.getTotalElements())
                .build())
            .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{clinicUserTaskId}")
    public ResponseEntity<Void> deleteClinicUserTask(@PathVariable("clinicUserTaskId") final Long clinicUserTaskId) {
        clinicUserTaskService.deleteClinicUserTask(clinicUserTaskId);
        return ResponseEntity.noContent().build();
    }

    private String appendClinicIdToFilter(final String filter, final Long clinicId) {
        final String modifiedFilter;
        if (StringUtils.hasText(filter)) {
            modifiedFilter = filter + ";clinic.id==" + clinicId;
        } else {
            modifiedFilter = "clinic.id==" + clinicId;
        }
        return modifiedFilter;
    }

}
