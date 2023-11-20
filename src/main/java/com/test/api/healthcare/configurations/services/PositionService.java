package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.Position.FK_POSITION_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.PositionQueryParam;
import com.test.api.healthcare.configurations.models.entities.Position;
import com.test.api.healthcare.configurations.repositories.PositionRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PositionService {

    private final PositionRepository positionRepository;

    public Position createPosition(final Position position) {
        try {
            return positionRepository.save(position);
        } catch (final DataIntegrityViolationException ex) {
            handleCreatePositionCreationFailure(ex, position);
            throw ex;
        }
    }

    private void handleCreatePositionCreationFailure(final DataIntegrityViolationException ex,
                                                                  final Position position) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_POSITION_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("position", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, position.getClinic().getId()));
            }
        }
    }

    public List<Position> listPositions(final PositionQueryParam positionQueryParam) {
        if (Objects.nonNull(positionQueryParam.getPositionType())) {
            return positionRepository
                .findAllByPositionTypeAndClinicId(positionQueryParam.getPositionType(), positionQueryParam.getClinicId());
        }

        return positionRepository.findAllByClinicId(positionQueryParam.getClinicId());
    }

    public void deletePosition(final Long positionId, final Long clinicId) {
        final Position position = positionRepository.findByIdAndClinicId(positionId, clinicId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ErrorMessage.ERR_MSG_POSITION_NOT_FOUND, positionId)));
        positionRepository.delete(position);
    }
}
