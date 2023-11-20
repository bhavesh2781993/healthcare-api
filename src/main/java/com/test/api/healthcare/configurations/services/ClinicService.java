package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.PageQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.repositories.ClinicRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public Clinic createClinic(final Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    public Optional<Clinic> getClinic(final Long clinicId) {
        return clinicRepository.findById(clinicId);
    }

    public void deleteClinic(final Long clinicId) {
        final Clinic clinic = clinicRepository.findById(clinicId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_NOT_FOUND, clinicId)));
        clinicRepository.delete(clinic);
    }

    public Page<Clinic> listClinics(final PageQueryParam pageQueryParam) {

        final Sort sortBy;
        if ("ASC".equalsIgnoreCase(pageQueryParam.getSortDirection())) {
            sortBy = Sort.by(pageQueryParam.getSortField()).ascending();
        } else {
            sortBy = Sort.by(pageQueryParam.getSortField()).descending();
        }

        final Pageable pageable = PageRequest.of(pageQueryParam.getPageNo(), pageQueryParam.getPageSize(), sortBy);
        return clinicRepository.findAll(pageable);
    }
}
