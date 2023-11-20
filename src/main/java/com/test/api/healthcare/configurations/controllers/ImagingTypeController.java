package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.ImagingTypeQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ImagingTypeMapper;
import com.test.api.healthcare.configurations.models.ImagingTypeModel;
import com.test.api.healthcare.configurations.models.entities.ImagingType;
import com.test.api.healthcare.configurations.services.ImagingTypeService;

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
@RequestMapping("/configurations/imaging-types")
public class ImagingTypeController {

    private final ImagingTypeService imagingTypeService;
    private final ImagingTypeMapper imagingTypeMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ImagingTypeModel>> createImagingType(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final ImagingTypeModel imagingTypeModel) {
        final ImagingType imagingType = imagingTypeMapper.toImagingType(imagingTypeModel, clinicId);
        final ImagingType createdImagingType = imagingTypeService.createImagingType(imagingType);
        final ImagingTypeModel createdImagingTypeModel =
            imagingTypeMapper.toImagingTypeModel(createdImagingType);
        final ApiResponse<ImagingTypeModel> response = ApiResponse.<ImagingTypeModel>builder()
            .data(createdImagingTypeModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ImagingTypeModel>> listImagingTypes(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final ImagingTypeQueryParam imagingTypeQueryParam = ImagingTypeQueryParam.builder()
            .clinicId(clinicId)
            .build();
        final List<ImagingType> imagingTypeList = imagingTypeService.listImagingTypes(imagingTypeQueryParam);
        final List<ImagingTypeModel> imagingTypeModelList =
            imagingTypeMapper.toImagingTypeModelList(imagingTypeList);
        final PaginatedApiResponse<ImagingTypeModel> response = PaginatedApiResponse.<ImagingTypeModel>builder()
            .data(imagingTypeModelList)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{imagingTypeId}")
    public ResponseEntity<ApiResponse<Void>> deleteImagingType(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("imagingTypeId") final Long imagingTypeId) {
        imagingTypeService.deleteImagingType(imagingTypeId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
