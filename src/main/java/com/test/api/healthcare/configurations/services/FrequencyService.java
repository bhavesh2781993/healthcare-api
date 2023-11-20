package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_FREQUENCY_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.Frequency.FK_FREQUENCY_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.configurations.models.entities.Frequency;
import com.test.api.healthcare.configurations.repositories.FrequencyRepository;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FrequencyService {

    private final FrequencyRepository frequencyRepository;

    public Frequency createFrequency(final Frequency frequencyToCreate) {
        try {
            return frequencyRepository.save(frequencyToCreate);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateFrequencyCreationFailure(ex, frequencyToCreate);
            throw ex;
        }
    }

    private void handleCreateFrequencyCreationFailure(final DataIntegrityViolationException ex,
                                                      final Frequency frequency) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_FREQUENCY_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("frequency", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, frequency.getClinic().getId()));
            }
        }
    }

    public List<Frequency> listFrequency(final Long clinicId) {
        return frequencyRepository.findAllByClinicId(clinicId);
    }

    public void deleteFrequency(final Long frequencyId) {
        final Frequency frequency = frequencyRepository.findById(frequencyId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_FREQUENCY_NOT_FOUND, frequencyId)));
        frequencyRepository.delete(frequency);
    }
}
