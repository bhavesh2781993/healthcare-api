package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.EnergyMapper;
import com.test.api.healthcare.configurations.models.EnergyModel;
import com.test.api.healthcare.configurations.models.entities.Energy;
import com.test.api.healthcare.configurations.services.EnergyService;

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
@RequestMapping("/configurations/energies")
public class EnergyController {

    private final EnergyService energyService;
    private final EnergyMapper energyMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<EnergyModel>> createEnergy(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final EnergyModel energyModel) {
        final Energy energyToCreate = energyMapper.toEnergy(energyModel, clinicId);
        final Energy createdEnergy = energyService.createEnergy(energyToCreate);
        final EnergyModel createdEnergyModel = energyMapper.toEnergyModel(createdEnergy);
        final ApiResponse<EnergyModel> response = ApiResponse.<EnergyModel>builder()
            .data(createdEnergyModel).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<EnergyModel>> listEnergy(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final List<Energy> energyList = energyService.listEnergy(clinicId);
        final List<EnergyModel> energyModelList = energyMapper.toEnergyModelList(energyList);
        final PaginatedApiResponse<EnergyModel> response = PaginatedApiResponse.<EnergyModel>builder()
            .data(energyModelList).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{energyId}")
    public ResponseEntity<ApiResponse<Void>> deleteEnergy(
        @PathVariable("energyId") final Long energyId) {
        energyService.deleteEnergy(energyId);
        return ResponseEntity.noContent().build();
    }

}
