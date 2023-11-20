package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.REQUEST_HEADER_CLINIC_ID;

import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.configurations.mappers.ConcurrentChemotherapyMapper;
import com.test.api.healthcare.configurations.models.ConcurrentChemotherapyModel;
import com.test.api.healthcare.configurations.models.entities.ConcurrentChemotherapy;
import com.test.api.healthcare.configurations.services.ConcurrentChemotherapyService;

import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/configurations/concurrent-chemotherapy")
public class ConcurrentChemotherapyController {

    private final ConcurrentChemotherapyMapper concurrentChemotherapyMapper;
    private final ConcurrentChemotherapyService concurrentChemotherapyService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConcurrentChemotherapyModel>> createConcurrentChemotherapy(
        @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final ConcurrentChemotherapyModel concurrentChemotherapyModel) {

        final ConcurrentChemotherapy concurrentChemotherapy =
            concurrentChemotherapyMapper.toConcurrentChemotherapy(concurrentChemotherapyModel, clinicId);
        final ConcurrentChemotherapy createdConcurrentChemotherapy =
            concurrentChemotherapyService.createConcurrentChemotherapy(concurrentChemotherapy);
        final ConcurrentChemotherapyModel createdConcurrentChemotherapyModel =
            concurrentChemotherapyMapper.toConcurrentChemotherapyModel(createdConcurrentChemotherapy);
        final ApiResponse<ConcurrentChemotherapyModel> response = ApiResponse.<ConcurrentChemotherapyModel>builder()
            .data(createdConcurrentChemotherapyModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConcurrentChemotherapyModel>>> listConcurrentChemotherapy(
        @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final List<ConcurrentChemotherapy> concurrentChemotherapyList =
            concurrentChemotherapyService.listConcurrentChemotherapy(clinicId);
        final List<ConcurrentChemotherapyModel> concurrentChemotherapyModelList =
            concurrentChemotherapyMapper.toConcurrentChemotherapyModelList(concurrentChemotherapyList);
        final ApiResponse<List<ConcurrentChemotherapyModel>> responses = ApiResponse.<List<ConcurrentChemotherapyModel>>builder()
            .data(concurrentChemotherapyModelList)
            .build();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{concurrentChemotherapyId}")
    public ResponseEntity<ApiResponse<Void>> deleteConcurrentChemotherapy(
        @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("concurrentChemotherapyId") final Long concurrentChemotherapyId) {
        concurrentChemotherapyService.deleteConcurrentChemotherapy(concurrentChemotherapyId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
