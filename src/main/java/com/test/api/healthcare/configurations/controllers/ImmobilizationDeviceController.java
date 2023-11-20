package com.test.api.healthcare.configurations.controllers;


import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.ImmobilizationDeviceQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ImmobilizationDeviceMapper;
import com.test.api.healthcare.configurations.models.ImmobilizationDeviceModel;
import com.test.api.healthcare.configurations.models.entities.ImmobilizationDevice;
import com.test.api.healthcare.configurations.services.ImmobilizationDeviceService;

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
@RequestMapping("/configurations/immobilization-devices")
public class ImmobilizationDeviceController {

    private final ImmobilizationDeviceService immobilizationDeviceService;
    private final ImmobilizationDeviceMapper immobilizationDeviceMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ImmobilizationDeviceModel>> createImmobilizationDevice(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final ImmobilizationDeviceModel immobilizationDeviceModel) {
        final ImmobilizationDevice immobilizationDevice =
            immobilizationDeviceMapper.toImmobilizationDevice(immobilizationDeviceModel, clinicId);
        final ImmobilizationDevice createdImmobilizationDevice =
            immobilizationDeviceService.createImmobilizationDevice(immobilizationDevice);

        final ImmobilizationDeviceModel createdImmobilizationDeviceModel =
            immobilizationDeviceMapper.toImmobilizationDeviceModel(createdImmobilizationDevice);

        final ApiResponse<ImmobilizationDeviceModel> response =
            ApiResponse.<ImmobilizationDeviceModel>builder()
                .data(createdImmobilizationDeviceModel)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ImmobilizationDeviceModel>> listImmobilizationDevices(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final ImmobilizationDeviceQueryParam immobilizationDeviceQueryParam = ImmobilizationDeviceQueryParam.builder()
            .clinicId(clinicId)
            .build();

        final List<ImmobilizationDevice> immobilizationDeviceList =
            immobilizationDeviceService.listImmobilizationDevices(immobilizationDeviceQueryParam);
        final List<ImmobilizationDeviceModel> immobilizationDeviceModelList =
            immobilizationDeviceMapper.toImmobilizationDeviceModelList(immobilizationDeviceList);
        final PaginatedApiResponse<ImmobilizationDeviceModel> response = PaginatedApiResponse.<ImmobilizationDeviceModel>builder()
            .data(immobilizationDeviceModelList)
            .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{immobilizationDeviceId}")
    public ResponseEntity<ApiResponse<Void>> deleteImmobilizationDevice(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("immobilizationDeviceId") final Long immobilizationDeviceId) {
        immobilizationDeviceService.deleteImmobilizationDevice(immobilizationDeviceId, clinicId);
        return ResponseEntity.noContent().build();
    }
}




