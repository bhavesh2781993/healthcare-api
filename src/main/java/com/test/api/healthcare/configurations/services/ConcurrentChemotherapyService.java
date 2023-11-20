package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.configurations.models.entities.ConcurrentChemotherapy.FK_CONCURRENT_CHEMOTHERAPY_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.configurations.models.entities.ConcurrentChemotherapy;
import com.test.api.healthcare.configurations.repositories.ConcurrentChemotherapyRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ConcurrentChemotherapyService {

    private final ConcurrentChemotherapyRepository concurrentChemotherapyRepository;

    public ConcurrentChemotherapy createConcurrentChemotherapy(final ConcurrentChemotherapy concurrentChemotherapy) {
        try {
            return concurrentChemotherapyRepository.save(concurrentChemotherapy);
        } catch (final DataIntegrityViolationException ex) {
            handleConcurrentChemotherapyCreationFailure(ex, concurrentChemotherapy);
            throw ex;
        }
    }

    private void handleConcurrentChemotherapyCreationFailure(final DataIntegrityViolationException ex,
                                                             final ConcurrentChemotherapy concurrentChemotherapy) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equalsIgnoreCase(FK_CONCURRENT_CHEMOTHERAPY_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("concurrentChemotherapy", "clinicId",
                    String.format(ErrorMessage.ERR_MSG_SIM_ORDER_NOT_FOUND, concurrentChemotherapy.getClinic().getId()));
            }
        }
    }

    public boolean getConcurrentChemotherapyByClinicId(final Long clinicId) {
        return concurrentChemotherapyRepository.existsByClinicId(clinicId);
    }

    public List<ConcurrentChemotherapy> listConcurrentChemotherapy(final Long clinicId) {
        return concurrentChemotherapyRepository.findAllByClinicId(clinicId);
    }

    public void deleteConcurrentChemotherapy(final Long concurrentChemotherapyId, final Long clinicId) {
        if (getConcurrentChemotherapyByClinicId(clinicId)) {
            final ConcurrentChemotherapy concurrentChemotherapy =
                concurrentChemotherapyRepository.findById(concurrentChemotherapyId)
                    .orElseThrow(() -> new DataNotFoundException(
                        String.format(ErrorMessage.ERR_MSG_CONCURRENT_CHEMOTHERAPY_NOT_FOUND, concurrentChemotherapyId)
                    ));
            concurrentChemotherapyRepository.delete(concurrentChemotherapy);
        } else {
            throw new DataNotFoundException(String.format(ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND, clinicId));
        }
    }
}
