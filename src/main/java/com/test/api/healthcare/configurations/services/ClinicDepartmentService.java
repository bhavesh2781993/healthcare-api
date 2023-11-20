package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_DEPARTMENT_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_LOCATION_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ClinicDepartment.FK_CLINIC_DEPARTMENT_CLINIC_LOCATION_ID;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ClinicQueryParam;
import com.test.api.healthcare.configurations.models.entities.ClinicDepartment;
import com.test.api.healthcare.configurations.repositories.ClinicDepartmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClinicDepartmentService {

    private final ClinicDepartmentRepository clinicDepartmentRepository;

    public ClinicDepartment createClinicDepartment(final ClinicDepartment clinicDepartmentToCreate) {
        try {
            return clinicDepartmentRepository.save(clinicDepartmentToCreate);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateClinicDepartmentCreationFailure(ex, clinicDepartmentToCreate);
            throw ex;
        }
    }

    private void handleCreateClinicDepartmentCreationFailure(final DataIntegrityViolationException ex,
                                                             final ClinicDepartment clinicDepartment) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_CLINIC_DEPARTMENT_CLINIC_LOCATION_ID)) {
                throw new InvalidFieldException("clinicDepartment", "clinicLocationId",
                    String.format(ERR_MSG_CLINIC_LOCATION_NOT_FOUND, clinicDepartment.getClinicLocation().getId()));
            }
        }
    }

    public Optional<ClinicDepartment> getClinicDepartment(final Long clinicDepartmentId) {
        return clinicDepartmentRepository.findById(clinicDepartmentId);
    }

    public Page<ClinicDepartment> listClinicDepartments(final ClinicQueryParam clinicQueryParam) {
        final Sort sortBy;
        if ("ASC".equalsIgnoreCase(clinicQueryParam.getSortDirection())) {
            sortBy = Sort.by(clinicQueryParam.getSortField()).ascending();
        } else {
            sortBy = Sort.by(clinicQueryParam.getSortField()).descending();
        }
        final Pageable pageable = PageRequest.of(clinicQueryParam.getPageNo(), clinicQueryParam.getPageSize(), sortBy);
        return clinicDepartmentRepository.findAllByClinicLocationClinicId(clinicQueryParam.getClinicId(), pageable);
    }

    public void deleteClinicDepartment(final Long clinicDepartmentId) {
        final ClinicDepartment clinicDepartment = clinicDepartmentRepository.findById(clinicDepartmentId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_DEPARTMENT_NOT_FOUND, clinicDepartmentId)));
        clinicDepartmentRepository.delete(clinicDepartment);
    }
}
