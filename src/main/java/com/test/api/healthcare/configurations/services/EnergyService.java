package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_ENERGY_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.Energy.FK_ENERGY_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.configurations.models.entities.Energy;
import com.test.api.healthcare.configurations.repositories.EnergyRepository;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EnergyService {

    private final EnergyRepository energyRepository;

    public Energy createEnergy(final Energy energyToCreate) {
        try {
            return energyRepository.save(energyToCreate);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateEnergyCreationFailure(ex, energyToCreate);
            throw ex;
        }
    }

    private void handleCreateEnergyCreationFailure(final DataIntegrityViolationException ex,
                                                   final Energy energy) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_ENERGY_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("energy", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, energy.getClinic().getId()));
            }
        }
    }

    public List<Energy> listEnergy(final Long clinicId) {
        return energyRepository.findAllByClinicId(clinicId);
    }

    public void deleteEnergy(final Long energyId) {
        final Energy energy = energyRepository.findById(energyId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_ENERGY_NOT_FOUND, energyId)));
        energyRepository.delete(energy);
    }
}
