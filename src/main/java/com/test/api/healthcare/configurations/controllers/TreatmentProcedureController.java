package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.TreatmentProcedureQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.TreatmentProcedureMapper;
import com.test.api.healthcare.configurations.models.TreatmentProcedureModel;
import com.test.api.healthcare.configurations.models.entities.TreatmentProcedure;
import com.test.api.healthcare.configurations.services.TreatmentProcedureService;

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
@RequestMapping("/configurations/treatment-procedures")
public class TreatmentProcedureController {

    private final TreatmentProcedureService treatmentProcedureService;
    private final TreatmentProcedureMapper treatmentProcedureMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<TreatmentProcedureModel>> createTreatmentProcedure(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final TreatmentProcedureModel treatmentProcedureModel) {
        final TreatmentProcedure treatmentProcedure = treatmentProcedureMapper.toTreatmentProcedure(treatmentProcedureModel, clinicId);
        final TreatmentProcedure createdTreatmentProcedure = treatmentProcedureService.createTreatmentProcedure(treatmentProcedure);
        final TreatmentProcedureModel createdTreatmentProcedureModel =
            treatmentProcedureMapper.toTreatmentProcedureModel(createdTreatmentProcedure);
        final ApiResponse<TreatmentProcedureModel> response = ApiResponse.<TreatmentProcedureModel>builder()
            .data(createdTreatmentProcedureModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<TreatmentProcedureModel>> listTreatmentProcedures(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final TreatmentProcedureQueryParam treatmentProcedureQueryParam = TreatmentProcedureQueryParam.builder()
            .clinicId(clinicId)
            .build();
        final List<TreatmentProcedure> treatmentProcedureList =
            treatmentProcedureService.listTreatmentProcedures(treatmentProcedureQueryParam);
        final List<TreatmentProcedureModel> treatmentProcedureModelList =
            treatmentProcedureMapper.toTreatmentProcedureModelList(treatmentProcedureList);
        final PaginatedApiResponse<TreatmentProcedureModel> response = PaginatedApiResponse.<TreatmentProcedureModel>builder()
            .data(treatmentProcedureModelList)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{treatmentProcedureId}")
    public ResponseEntity<ApiResponse<Void>> deleteTreatmentProcedure(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("treatmentProcedureId") final Long treatmentProcedureId) {
        treatmentProcedureService.deleteTreatmentProcedure(treatmentProcedureId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
