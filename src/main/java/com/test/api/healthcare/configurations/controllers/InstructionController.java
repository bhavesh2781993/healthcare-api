package com.test.api.healthcare.configurations.controllers;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.InstructionQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.InstructionMapper;
import com.test.api.healthcare.configurations.models.InstructionModel;
import com.test.api.healthcare.configurations.models.entities.Instruction;
import com.test.api.healthcare.configurations.services.InstructionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@RequiredArgsConstructor
@RestController
@RequestMapping("/configurations/instructions")
public class InstructionController {

    private final InstructionService instructionService;
    private final InstructionMapper instructionMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<InstructionModel>> createInstruction(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final InstructionModel instructionModel) {
        final Instruction instruction = instructionMapper.toInstruction(instructionModel, clinicId);
        final Instruction createdInstruction = instructionService.createInstruction(instruction);
        final InstructionModel createdInstructionModel =
            instructionMapper.toInstructionModel(createdInstruction);
        final ApiResponse<InstructionModel> response = ApiResponse.<InstructionModel>builder()
            .data(createdInstructionModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<InstructionModel>> listInstructions(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        final InstructionQueryParam instructionQueryParam = InstructionQueryParam.builder()
            .clinicId(clinicId)
            .build();

        final List<Instruction> instruction =
            instructionService.listInstruction(instructionQueryParam);

        final List<InstructionModel> instructionModelList =
            instructionMapper.toInstructionModelList(instruction);

        final PaginatedApiResponse<InstructionModel> responses =
            PaginatedApiResponse.<InstructionModel>builder()
            .data(instructionModelList)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/{instructionId}")
    public ResponseEntity<ApiResponse<Void>> deleteInstruction(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @PathVariable("instructionId") final Long instructionId) {
        instructionService.deleteInstruction(instructionId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
