package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.REQUEST_HEADER_CLINIC_ID;

import com.test.api.healthcare.common.models.PositionQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.common.validators.ValidateEnum;
import com.test.api.healthcare.configurations.constants.PositionType;
import com.test.api.healthcare.configurations.mappers.PositionMapper;
import com.test.api.healthcare.configurations.models.PositionModel;
import com.test.api.healthcare.configurations.models.entities.Position;
import com.test.api.healthcare.configurations.services.PositionService;

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
@RequestMapping("/configurations/positions")
public class PositionController {

    private final PositionMapper positionMapper;
    private final PositionService positionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PositionModel>> createPosition(
        @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestBody @Valid final PositionModel positionModel) {
        final Position positionToCreate = positionMapper.toPosition(positionModel, clinicId);
        final Position createdPosition = positionService.createPosition(positionToCreate);
        final PositionModel createdPositionModel = positionMapper.toPositionModel(createdPosition);
        final ApiResponse<PositionModel> response = ApiResponse.<PositionModel>builder()
            .data(createdPositionModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedApiResponse<PositionModel>> listPositions(
        @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestParam(name = "positionType", required = false)
        @ValidateEnum(type = PositionType.class, isNullable = true)
        final String positionType) {

        final PositionQueryParam positionQueryParam = PositionQueryParam.builder()
            .clinicId(clinicId)
            .positionType(positionType)
            .build();

        final List<Position> positionList = positionService.listPositions(positionQueryParam);
        final List<PositionModel> positionModelList = positionMapper.toPositionModelList(positionList);
        final PaginatedApiResponse<PositionModel> response = PaginatedApiResponse.<PositionModel>builder()
            .data(positionModelList)
            .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{positionId}")
    public ResponseEntity<Void> deletePosition(@PathVariable final Long positionId,
                                               @RequestHeader(name = REQUEST_HEADER_CLINIC_ID) final Long clinicId) {
        positionService.deletePosition(positionId, clinicId);
        return ResponseEntity.noContent().build();
    }
}
