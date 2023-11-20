package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.PAGE_SIZE;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT;

import com.test.api.healthcare.common.constants.ApplicationConstant;
import com.test.api.healthcare.common.models.FiqlQueryParam;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.validators.FileSize;
import com.test.api.healthcare.configurations.mappers.ClinicUserMapper;
import com.test.api.healthcare.configurations.models.ClinicUserModel;
import com.test.api.healthcare.configurations.models.entities.ClinicUser;
import com.test.api.healthcare.configurations.services.ClinicUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class ClinicUserController {

    private final ClinicUserService clinicUserService;
    private final ClinicUserMapper clinicUserMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ClinicUserModel>> createClinicUser(
        @RequestPart("clinicUser") @Valid final ClinicUserModel clinicUser,
        @RequestPart("image") @FileSize(maxSizeInMB = 2) final MultipartFile image) {
        final ClinicUser clinicUserToCreate = clinicUserMapper.toClinicUser(clinicUser, image);
        final ClinicUser createdClinicUser = clinicUserService.createClinicUser(clinicUserToCreate);
        final ClinicUserModel createdClinicUserModel = clinicUserMapper.toClinicUserModel(createdClinicUser);
        final ApiResponse<ClinicUserModel> response = ApiResponse.<ClinicUserModel>builder()
            .data(createdClinicUserModel).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{clinicUserId}")
    public ResponseEntity<ApiResponse<ClinicUserModel>> updateClinicUser(
        @PathVariable("clinicUserId") final Long clinicUserId,
        @RequestPart("clinicUser") final ClinicUserModel clinicUser,
        @RequestPart("image") @FileSize(maxSizeInMB = 2) final MultipartFile image) {
        final ClinicUser matchingClinicUser = clinicUserMapper.toClinicUser(clinicUserId, clinicUser, image);
        final ClinicUser updatedClinicUser = clinicUserService.updateClinicUser(matchingClinicUser);
        final ClinicUserModel clinicUserModel = clinicUserMapper.toClinicUserModel(updatedClinicUser);
        final ApiResponse<ClinicUserModel> response = ApiResponse.<ClinicUserModel>builder()
            .data(clinicUserModel)
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clinicUserId}")
    public ResponseEntity<ApiResponse<ClinicUserModel>> getClinicUser(
        @RequestParam(value = "includeImage", defaultValue = "false") final Boolean includeImage,
        @PathVariable("clinicUserId") final Long clinicUserid) {
        final ClinicUser clinicUser = clinicUserService.getClinicUser(clinicUserid, includeImage);
        final ClinicUserModel clinicUserModel = clinicUserMapper.toClinicUserModel(clinicUser);
        final ApiResponse<ClinicUserModel> response = ApiResponse.<ClinicUserModel>builder()
            .data(clinicUserModel)
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClinicUserModel>>> listClinicUsers(
        @RequestHeader(name = ApplicationConstant.REQUEST_HEADER_CLINIC_ID) final Long clinicId,
        @RequestParam(name = "filter", required = false) final String filter,
        @RequestParam(name = "sort", required = false) final String sort,
        @RequestParam(name = "pageNo", defaultValue = "0") final int pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10")
        @Max(value = PAGE_SIZE, message = ERR_MSG_PAGE_SIZE_EXCEEDS_ALLOWED_LIMIT) final int pageSize) {

        final String modifiedFilter = appendClinicIdToFilter(filter, clinicId);
        final FiqlQueryParam queryParam = FiqlQueryParam.builder()
            .filter(modifiedFilter)
            .sort(sort)
            .pageNo(pageNo)
            .pageSize(pageSize)
            .build();
        final List<ClinicUser> clinicUserList = clinicUserService.listClinicUsers(queryParam);
        final List<ClinicUserModel> clinicUserModels = clinicUserMapper.toClinicUserModelList(clinicUserList);
        final ApiResponse<List<ClinicUserModel>> response = ApiResponse.<List<ClinicUserModel>>builder()
            .data(clinicUserModels)
            .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{clinicUserId}")
    public ResponseEntity<Void> deleteClinicUser(@PathVariable("clinicUserId") final Long clinicUserId) {
        clinicUserService.deleteClinicUser(clinicUserId);
        return ResponseEntity.noContent().build();
    }

    private String appendClinicIdToFilter(final String filter, final Long clinicId) {
        final String modifiedFilter;
        if (StringUtils.hasText(filter)) {
            modifiedFilter = filter + ";clinicDepartment.clinicLocation.clinic.id==" + clinicId;
        } else {
            modifiedFilter = "clinicDepartment.clinicLocation.clinic.id==" + clinicId;
        }
        return modifiedFilter;
    }
}
