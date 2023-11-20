package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.TreatmentStageQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.StageType;
import com.test.api.healthcare.configurations.mappers.TreatmentStageMapper;
import com.test.api.healthcare.configurations.models.TreatmentStageModel;
import com.test.api.healthcare.configurations.models.entities.TreatmentStage;
import com.test.api.healthcare.configurations.services.TreatmentStageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/configurations/treatment-stages")
public class TreatmentStageController {

    private final TreatmentStageService treatmentStageService;
    private final TreatmentStageMapper treatmentStageMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<TreatmentStageModel>> createTreatmentStage(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final TreatmentStageModel treatmentStageModel) {
        final TreatmentStage treatmentStage = treatmentStageMapper.toTreatmentStage(treatmentStageModel, clinicId);
        final TreatmentStage createdTreatmentStage = treatmentStageService.createTreatmentStage(treatmentStage);
        final TreatmentStageModel createdTreatmentStageModel = treatmentStageMapper.toTreatmentStageModel(createdTreatmentStage);
        final ApiResponse<TreatmentStageModel> response = ApiResponse.<TreatmentStageModel>builder()
            .data(createdTreatmentStageModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<TreatmentStageModel>> listTreatmentStages(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestParam(name = "stageType", required = false)
        @ValidateEnum(type = StageType.class, isNullable = true)
        final String stageType) {

        final TreatmentStageQueryParam treatmentStageQueryParam = TreatmentStageQueryParam.builder()
            .clinicId(clinicId)
            .stageType(stageType)
            .build();
        final List<TreatmentStage> treatmentStagesList = treatmentStageService.listTreatmentStages(treatmentStageQueryParam);
        final List<TreatmentStageModel> treatmentStageModelList = treatmentStageMapper.toTreatmentStageModelList(treatmentStagesList);
        final PaginatedApiResponse<TreatmentStageModel> response = PaginatedApiResponse.<TreatmentStageModel>builder()
            .data(treatmentStageModelList).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{treatmentStageId}")
    public ResponseEntity<ApiResponse<TreatmentStageModel>> getTreatmentStage(@PathVariable("treatmentStageId") final Long treatmentId) {
        final TreatmentStage treatmentStage = treatmentStageService.getTreatmentStage(treatmentId)
            .orElseThrow(() -> new DataNotFoundException("Field cannot be exists" + treatmentId));
        final TreatmentStageModel treatmentStageModel = treatmentStageMapper.toTreatmentStageModel(treatmentStage);
        final ApiResponse<TreatmentStageModel> response = ApiResponse.<TreatmentStageModel>builder()
            .data(treatmentStageModel)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{treatmentStageId}")
    public ResponseEntity<Void> deleteByIdAndClinicId(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("treatmentStageId") final Long treatmentStageId) {
        treatmentStageService.deleteTreatmentStage(treatmentStageId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
