package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.FusionQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.FusionMapper;
import com.test.api.healthcare.configurations.models.FusionModel;
import com.test.api.healthcare.configurations.models.entities.Fusion;
import com.test.api.healthcare.configurations.services.FusionService;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RestController
@RequestMapping("/configurations/fusions")
public class FusionController {

    private final FusionService fusionService;
    private final FusionMapper fusionMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<FusionModel>> createFusion(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final FusionModel fusionModel) {
        final Fusion fusion = fusionMapper.toFusion(fusionModel, clinicId);
        final Fusion createdFusion = fusionService.createFusion(fusion);
        final FusionModel createdModel = fusionMapper.toFusionModel(createdFusion);
        final ApiResponse<FusionModel> response = ApiResponse.<FusionModel>builder()
            .data(createdModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<FusionModel>> listFusions(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final FusionQueryParam fusionQueryParam = FusionQueryParam.builder()
            .clinicId(clinicId)
            .build();
        final List<Fusion> fusion = fusionService.listFusion(fusionQueryParam);
        final List<FusionModel> fusionModelList = fusionMapper.toFusionModelList(fusion);
        final PaginatedApiResponse<FusionModel> response = PaginatedApiResponse.<FusionModel>builder()
            .data(fusionModelList)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{fusionId}")
    public ResponseEntity<ApiResponse<Void>> deleteFusion(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("fusionId") @Valid final  Long fusionId) {
        fusionService.deleteFusion(fusionId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
