package com.test.api.healthcare.configurations.services;


import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_TREATMENT_META_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.TreatmentMeta.FK_TREATMENT_META_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.TreatmentMetaQueryParam;
import com.test.api.healthcare.configurations.constants.TreatmentMetaType;
import com.test.api.healthcare.configurations.models.entities.TreatmentMeta;
import com.test.api.healthcare.configurations.repositories.TreatmentMetaRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TreatmentMetaService {

    private final TreatmentMetaRepository treatmentMetaRepository;

    public TreatmentMeta createTreatmentMeta(final TreatmentMeta treatmentMeta) {
        try {
            return treatmentMetaRepository.save(treatmentMeta);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateTreatmentMetaCreationFailure(ex, treatmentMeta);
            throw ex;
        }
    }

    private void handleCreateTreatmentMetaCreationFailure(final DataIntegrityViolationException ex,
                                                     final TreatmentMeta treatmentMeta) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_TREATMENT_META_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("treatmentMeta", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, treatmentMeta.getClinic().getId()));
            }
        }
    }

    public List<TreatmentMeta> listTreatmentMeta(final TreatmentMetaQueryParam treatmentMetaQueryParam) {
        if (Objects.nonNull(treatmentMetaQueryParam.getMetaType())) {
            return treatmentMetaRepository
                .findAllByMetaTypeAndClinicId(treatmentMetaQueryParam.getMetaType(), treatmentMetaQueryParam.getClinicId());
        }
        return treatmentMetaRepository.findAllByClinicId(treatmentMetaQueryParam.getClinicId());
    }

    public void deleteTreatmentMeta(final Long treatmentMetaId, final Long clinicId) {
        final TreatmentMeta treatmentMeta =
            treatmentMetaRepository.findByIdAndClinicId(treatmentMetaId, clinicId)
                .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_TREATMENT_META_NOT_FOUND, treatmentMetaId, clinicId)));
        treatmentMetaRepository.delete(treatmentMeta);
    }

    public boolean existsByTreatmentMetaIdAndType(final Long treatmentMetaId, final TreatmentMetaType treatmentMetaType) {
        return treatmentMetaRepository.existsByIdAndMetaType(treatmentMetaId, treatmentMetaType.name());
    }
}
