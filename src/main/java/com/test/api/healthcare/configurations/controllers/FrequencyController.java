package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.FrequencyMapper;
import com.test.api.healthcare.configurations.models.FrequencyModel;
import com.test.api.healthcare.configurations.models.entities.Frequency;
import com.test.api.healthcare.configurations.services.FrequencyService;

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
@RequestMapping("/configurations/frequencies")
public class FrequencyController {

    private final FrequencyService frequencyService;
    private final FrequencyMapper frequencyMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<FrequencyModel>> createFrequency(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final FrequencyModel frequencyModel) {
        final Frequency frequencyToCreate = frequencyMapper.toFrequency(frequencyModel, clinicId);
        final Frequency createdFrequency = frequencyService.createFrequency(frequencyToCreate);
        final FrequencyModel createdFrequencyModel = frequencyMapper.toFrequencyModel(createdFrequency);
        final ApiResponse<FrequencyModel> response = ApiResponse.<FrequencyModel>builder()
            .data(createdFrequencyModel).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<FrequencyModel>> listFrequency(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final List<Frequency> frequencyList = frequencyService.listFrequency(clinicId);
        final List<FrequencyModel> frequencyModelList = frequencyMapper.toFrequencyModelList(frequencyList);
        final PaginatedApiResponse<FrequencyModel> response = PaginatedApiResponse.<FrequencyModel>builder()
            .data(frequencyModelList).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{frequencyId}")
    public ResponseEntity<ApiResponse<Void>> deleteFrequency(
        @PathVariable("frequencyId") @Valid final Long frequencyId) {
        frequencyService.deleteFrequency(frequencyId);
        return ResponseEntity.noContent().build();
    }
    
}
