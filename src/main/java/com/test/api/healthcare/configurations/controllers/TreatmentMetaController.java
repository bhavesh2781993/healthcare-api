package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.REQUEST_HEADER_CLINIC_ID;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.TreatmentMetaQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.TreatmentMetaType;
import com.test.api.healthcare.configurations.mappers.TreatmentMetaMapper;
import com.test.api.healthcare.configurations.models.TreatmentMetaModel;
import com.test.api.healthcare.configurations.models.entities.TreatmentMeta;
import com.test.api.healthcare.configurations.services.TreatmentMetaService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/configurations/treatment-meta")
public class TreatmentMetaController {

    private final TreatmentMetaService treatmentMetaService;
    private final TreatmentMetaMapper treatmentMetaMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<TreatmentMetaModel>> createTreatmentMeta(
        @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final TreatmentMetaModel treatmentMetaModel) {
        final TreatmentMeta treatmentMeta = treatmentMetaMapper.toTreatmentMeta(treatmentMetaModel, clinicId);
        final TreatmentMeta createdTreatmentMeta = treatmentMetaService.createTreatmentMeta(treatmentMeta);
        final TreatmentMetaModel createdTreatmentMetaModel = treatmentMetaMapper.toTreatmentMetaModel(createdTreatmentMeta);
        final ApiResponse<TreatmentMetaModel> response = ApiResponse.<TreatmentMetaModel>builder()
            .data(createdTreatmentMetaModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<TreatmentMetaModel>> listTreatmentMeta(
        @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestParam(name = "metaType", required = false)
        @ValidateEnum(type = TreatmentMetaType.class, isNullable = true)
        final String metaType) {
        final TreatmentMetaQueryParam treatmentMetaQueryParam = TreatmentMetaQueryParam.builder()
            .clinicId(clinicId)
            .metaType(metaType)
            .build();

        final List<TreatmentMeta> treatmentMetaList = treatmentMetaService.listTreatmentMeta(treatmentMetaQueryParam);
        final List<TreatmentMetaModel> treatmentMetaModelList = treatmentMetaMapper.toTreatmentMetaModelList(treatmentMetaList);
        final PaginatedApiResponse<TreatmentMetaModel> response = PaginatedApiResponse.<TreatmentMetaModel>builder()
            .data(treatmentMetaModelList)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{treatmentMetaId}")
    public ResponseEntity<Void> deleteTreatmentMeta(
        @PathVariable final Long treatmentMetaId,
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        treatmentMetaService.deleteTreatmentMeta(treatmentMetaId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
