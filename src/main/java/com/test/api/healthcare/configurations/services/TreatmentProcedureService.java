package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.TreatmentProcedure.FK_TREATMENT_PROCEDURE_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.TreatmentProcedureQueryParam;
import com.test.api.healthcare.configurations.models.entities.TreatmentProcedure;
import com.test.api.healthcare.configurations.repositories.TreatmentProcedureRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TreatmentProcedureService {

    private final TreatmentProcedureRepository treatmentProcedureRepository;

    public TreatmentProcedure createTreatmentProcedure(final TreatmentProcedure treatmentProcedure) {
        try {
            return treatmentProcedureRepository.save(treatmentProcedure);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateTreatmentProcedureCreationFailure(ex, treatmentProcedure);
            throw ex;
        }
    }

    private void handleCreateTreatmentProcedureCreationFailure(final DataIntegrityViolationException ex,
                                                          final TreatmentProcedure treatmentProcedure) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_TREATMENT_PROCEDURE_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("treatmentProcedure", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, treatmentProcedure.getClinic().getId()));
            }
        }
    }

    public List<TreatmentProcedure> listTreatmentProcedures(final TreatmentProcedureQueryParam treatmentProcedureQueryParam) {
        return treatmentProcedureRepository.findAllByClinicId(treatmentProcedureQueryParam.getClinicId());
    }

    @Transactional
    public void deleteTreatmentProcedure(final Long treatmentProcedureId, final Long clinicId) {
        final TreatmentProcedure treatmentProcedure = treatmentProcedureRepository.findByIdAndClinicId(treatmentProcedureId, clinicId)
            .orElseThrow(() -> new DataNotFoundException(String
                .format(ErrorMessage.ERR_MSG_TREATMENT_PROCEDURE_NOT_FOUND, treatmentProcedureId, clinicId)));
        treatmentProcedureRepository.delete(treatmentProcedure);
    }
}
