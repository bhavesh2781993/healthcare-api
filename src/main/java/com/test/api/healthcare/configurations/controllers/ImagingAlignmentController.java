package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.ImagingAlignmentQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.ImagingAlignmentMapper;
import com.test.api.healthcare.configurations.models.ImagingAlignmentModel;
import com.test.api.healthcare.configurations.models.entities.ImagingAlignment;
import com.test.api.healthcare.configurations.services.ImagingAlignmentService;

import lombok.RequiredArgsConstructor;
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

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("configurations/imaging-alignments")
public class ImagingAlignmentController {

    private final ImagingAlignmentService imagingAlignmentService;
    private final ImagingAlignmentMapper imagingAlignmentMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ImagingAlignmentModel>> createImagingAlignment(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody final ImagingAlignmentModel imagingAlignmentModel) {
        final ImagingAlignment imagingAlignment =
            imagingAlignmentMapper.toImagingAlignment(imagingAlignmentModel, clinicId);
        final ImagingAlignment createdImagingAlignment =
            imagingAlignmentService.createImagingAlignment(imagingAlignment);
        final ImagingAlignmentModel createdImagingAlignmentModel =
            imagingAlignmentMapper.toImagingAlignmentModel(createdImagingAlignment);
        final ApiResponse<ImagingAlignmentModel> response = ApiResponse.<ImagingAlignmentModel>builder()
            .data(createdImagingAlignmentModel)
            .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ImagingAlignmentModel>> listImagingAlignments(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final ImagingAlignmentQueryParam imagingAlignmentQueryParam = ImagingAlignmentQueryParam.builder()
            .clinicId(clinicId)
            .build();
        final List<ImagingAlignment> imagingAlignments =
            imagingAlignmentService.listImagingAlignment(imagingAlignmentQueryParam);
        final List<ImagingAlignmentModel> imagingAlignmentModels =
            imagingAlignmentMapper.toImagingAlignmentModelList(imagingAlignments);
        final PaginatedApiResponse<ImagingAlignmentModel> response = PaginatedApiResponse.<ImagingAlignmentModel>builder()
            .data(imagingAlignmentModels)
            .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{imagingAlignmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteImagingAlignment(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("imagingAlignmentId") final Long imagingAlignmentId) {
        imagingAlignmentService.deleteImagingAlignment(imagingAlignmentId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
