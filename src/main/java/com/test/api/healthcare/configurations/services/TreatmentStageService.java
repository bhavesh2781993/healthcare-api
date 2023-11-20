package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_TREATMENT_STAGE_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.TreatmentStage.FK_TNM_STAGE_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.TreatmentStageQueryParam;
import com.test.api.healthcare.configurations.models.entities.TreatmentStage;
import com.test.api.healthcare.configurations.repositories.TreatmentStageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TreatmentStageService {

    private final TreatmentStageRepository treatmentStageRepository;

    public TreatmentStage createTreatmentStage(final TreatmentStage treatmentStage) {
        try {
            return treatmentStageRepository.save(treatmentStage);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateTreatmentStageCreationFailure(ex, treatmentStage);
            throw ex;
        }
    }

    private void handleCreateTreatmentStageCreationFailure(final DataIntegrityViolationException ex,
                                                           final TreatmentStage treatmentStage) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_TNM_STAGE_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("treatmentStage", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, treatmentStage.getClinic().getId()));
            }
        }
    }

    public List<TreatmentStage> listTreatmentStages(final TreatmentStageQueryParam treatmentStageQueryParam) {
        if (Objects.nonNull(treatmentStageQueryParam.getStageType())) {
            return treatmentStageRepository.findAllByStageTypeAndClinicId(treatmentStageQueryParam.getStageType(),
                treatmentStageQueryParam.getClinicId());
        }

        return treatmentStageRepository.findAllByClinicId(treatmentStageQueryParam.getClinicId());
    }

    public Optional<TreatmentStage> getTreatmentStage(final Long treatmentStageId) {
        return treatmentStageRepository.findById(treatmentStageId);
    }

    @Transactional
    public void deleteTreatmentStage(final Long treatmentStageId, final Long clinicId) {
        final TreatmentStage treatmentStage = treatmentStageRepository.findByIdAndClinicId(treatmentStageId, clinicId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_TREATMENT_STAGE_NOT_FOUND, treatmentStageId, clinicId)));
        treatmentStageRepository.delete(treatmentStage);
    }
}
