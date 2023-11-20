package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.Fusion.FK_FUSION_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.FusionQueryParam;
import com.test.api.healthcare.configurations.models.entities.Fusion;
import com.test.api.healthcare.configurations.repositories.FusionRepository;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class FusionService {

    private final FusionRepository fusionRepository;

    public Fusion createFusion(final Fusion fusion) {
        try {
            return fusionRepository.save(fusion);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateFusionCreationFailure(ex, fusion);
            throw ex;
        }
    }

    private void handleCreateFusionCreationFailure(final DataIntegrityViolationException ex,
                                                   final Fusion fusion) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_FUSION_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("fusion", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, fusion.getClinic().getId()));
            }
        }
    }

    public List<Fusion> listFusion(final FusionQueryParam fusionsQueryParam) {
        return fusionRepository.findAllByClinicId(fusionsQueryParam.getClinicId());
    }

    @Transactional
    public void deleteFusion(final Long fusionId, final Long clinicId) {
        final Fusion fusion = fusionRepository.findByIdAndClinicId(fusionId, clinicId)
            .orElseThrow(() -> new DataNotFoundException(String
                .format(ErrorMessage.ERR_MSG_FUSION_NOT_FOUND, fusionId, clinicId)));
        fusionRepository.delete(fusion);
    }

}
