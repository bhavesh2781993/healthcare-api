package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.MedicalPhysicsConsultQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.MedicalPhysicsConsultMapper;
import com.test.api.healthcare.configurations.models.MedicalPhysicsConsultModel;
import com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult;
import com.test.api.healthcare.configurations.services.MedicalPhysicsConsultService;

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
@RequestMapping("/configurations/medical-physics-consults")
public class MedicalPhysicsConsultController {

    private final MedicalPhysicsConsultService medicalPhysicsConsultService;

    private final MedicalPhysicsConsultMapper medicalPhysicsConsultMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicalPhysicsConsultModel>> createMedicalPhysicsConsult(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final MedicalPhysicsConsultModel medicalPhysicsConsultModel) {
        final MedicalPhysicsConsult medicalPhysicsConsult =
            medicalPhysicsConsultMapper.toMedicalPhysicsConsult(medicalPhysicsConsultModel, clinicId);
        final MedicalPhysicsConsult createdMedicalPhysicsConsult =
            medicalPhysicsConsultService.createMedicalPhysicsConsult(medicalPhysicsConsult);
        final MedicalPhysicsConsultModel createdMedicalPhysicsConsultModel =
            medicalPhysicsConsultMapper.toMedicalPhysicsConsultModel(createdMedicalPhysicsConsult);
        final ApiResponse<MedicalPhysicsConsultModel> response = ApiResponse.<MedicalPhysicsConsultModel>builder()
            .data(createdMedicalPhysicsConsultModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<MedicalPhysicsConsultModel>> listMedicalPhysicsConsults(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final MedicalPhysicsConsultQueryParam medicalPhysicsConsultQueryParam = MedicalPhysicsConsultQueryParam.builder()
            .clinicId(clinicId)
            .build();

        final List<MedicalPhysicsConsult> medicalPhysicsConsultList =
            medicalPhysicsConsultService.listMedicalPhysicsConsults(medicalPhysicsConsultQueryParam);

        final List<MedicalPhysicsConsultModel> medicalPhysicsConsultModelsList =
            medicalPhysicsConsultMapper.toMedicalPhysicsConsultModelList(medicalPhysicsConsultList);

        final PaginatedApiResponse<MedicalPhysicsConsultModel> response =
            PaginatedApiResponse.<MedicalPhysicsConsultModel>builder()
            .data(medicalPhysicsConsultModelsList)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{medicalPhysicsConsultId}")
    public ResponseEntity<ApiResponse<MedicalPhysicsConsultModel>> deleteMedicalPhysicsConsult(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("medicalPhysicsConsultId") final Long medicalPhysicsConsultId) {
        medicalPhysicsConsultService.deleteMedicalPhysicsConsult(medicalPhysicsConsultId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
