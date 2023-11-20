package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult.FK_MEDICAL_PHYSICS_CONSULT_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.MedicalPhysicsConsultQueryParam;
import com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult;
import com.test.api.healthcare.configurations.repositories.MedicalPhysicsConsultRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MedicalPhysicsConsultService {

    private final MedicalPhysicsConsultRepository medicalPhysicsConsultRepository;

    public MedicalPhysicsConsult createMedicalPhysicsConsult(final MedicalPhysicsConsult medicalPhysicsConsult) {
        try {
            return medicalPhysicsConsultRepository.save(medicalPhysicsConsult);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateMedicalPhysicsConsultCreationFailure(ex, medicalPhysicsConsult);
            throw ex;
        }
    }

    private void handleCreateMedicalPhysicsConsultCreationFailure(final DataIntegrityViolationException ex,
                                                                 final MedicalPhysicsConsult medicalPhysicsConsult) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_MEDICAL_PHYSICS_CONSULT_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("medicalPhysicsConsult", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, medicalPhysicsConsult.getClinic().getId()));
            }
        }
    }

    public List<MedicalPhysicsConsult> listMedicalPhysicsConsults(
        final MedicalPhysicsConsultQueryParam medicalPhysicsConsultQueryParam) {
        return medicalPhysicsConsultRepository.findByClinicId(medicalPhysicsConsultQueryParam.getClinicId());
    }

    @Transactional
    public void deleteMedicalPhysicsConsult(final Long medicalPhysicsConsultId, final Long clinicId) {
        final MedicalPhysicsConsult medicalPhysicsConsult =
            medicalPhysicsConsultRepository.findByIdAndClinicId(medicalPhysicsConsultId, clinicId)
                .orElseThrow(() -> new DataNotFoundException(String
                    .format(ErrorMessage.ERR_MSG_MEDICAL_PHYSICS_CONSULT_NOT_FOUND, medicalPhysicsConsultId, clinicId)));

        medicalPhysicsConsultRepository.delete(medicalPhysicsConsult);
    }
}


