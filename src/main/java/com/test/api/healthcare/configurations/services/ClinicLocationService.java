package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_LOCATION_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ClinicLocation.FK_CLINIC_LOCATION_CLINIC_ID;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ClinicQueryParam;
import com.test.api.healthcare.configurations.models.entities.ClinicLocation;
import com.test.api.healthcare.configurations.repositories.ClinicLocationRepository;

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
public class ClinicLocationService {

    private final ClinicLocationRepository clinicLocationRepository;

    public ClinicLocation createClinicLocation(final ClinicLocation clinicLocationToCreate) {
        try {
            return clinicLocationRepository.save(clinicLocationToCreate);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateClinicLocationCreationFailure(ex, clinicLocationToCreate);
            throw ex;
        }
    }

    private void handleCreateClinicLocationCreationFailure(final DataIntegrityViolationException ex,
                                                           final ClinicLocation clinicLocation) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_CLINIC_LOCATION_CLINIC_ID)) {
                throw new InvalidFieldException("clinicLocation", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, clinicLocation.getClinic().getId()));
            }
        }
    }

    public Optional<ClinicLocation> getClinicLocation(final Long clinicLocationId) {
        return clinicLocationRepository.findById(clinicLocationId);
    }

    public Page<ClinicLocation> listClinicLocations(final ClinicQueryParam clinicQueryParam) {
        final Sort sortBy;
        if ("ASC".equalsIgnoreCase(clinicQueryParam.getSortDirection())) {
            sortBy = Sort.by(clinicQueryParam.getSortField()).ascending();
        } else {
            sortBy = Sort.by(clinicQueryParam.getSortField()).descending();
        }
        final Pageable pageable = PageRequest.of(clinicQueryParam.getPageNo(), clinicQueryParam.getPageSize(), sortBy);
        return clinicLocationRepository.findAllByClinicId(clinicQueryParam.getClinicId(), pageable);
    }

    public void deleteClinicLocation(final Long clinicLocationId) {
        final ClinicLocation clinicLocation = clinicLocationRepository.findById(clinicLocationId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_LOCATION_NOT_FOUND, clinicLocationId)));
        clinicLocationRepository.delete(clinicLocation);
    }

}
