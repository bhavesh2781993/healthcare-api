package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ImmobilizationDevice.FK_IMMOBILIZATION_DEVICE_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ImmobilizationDeviceQueryParam;
import com.test.api.healthcare.configurations.models.entities.ImmobilizationDevice;
import com.test.api.healthcare.configurations.repositories.ImmobilizationDeviceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImmobilizationDeviceService {

    private final ImmobilizationDeviceRepository immobilizationDeviceRepository;

    public ImmobilizationDevice createImmobilizationDevice(final ImmobilizationDevice immobilizationDevice) {
        try {
            return immobilizationDeviceRepository.save(immobilizationDevice);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateImmobilizationDeviceCreationFailure(ex, immobilizationDevice);
            throw ex;
        }
    }

    private void handleCreateImmobilizationDeviceCreationFailure(final DataIntegrityViolationException ex,
                                                        final ImmobilizationDevice immobilizationDevice) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_IMMOBILIZATION_DEVICE_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("immobilizationDevice", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, immobilizationDevice.getClinic().getId()));
            }
        }
    }

    public List<ImmobilizationDevice> listImmobilizationDevices(final ImmobilizationDeviceQueryParam immobilizationDeviceQueryParam) {
        return immobilizationDeviceRepository.findAllByClinicId(immobilizationDeviceQueryParam.getClinicId());
    }

    @Transactional
    public void deleteImmobilizationDevice(final Long immobilizationDeviceId, final Long clinicId) {
        final ImmobilizationDevice immobilizationDevice =
            immobilizationDeviceRepository.findByIdAndClinicId(immobilizationDeviceId, clinicId)
                .orElseThrow(() -> new DataValidationException(String.format(ErrorMessage.ERR_MSG_IMMOBILIZATION_DEVICE_NOT_FOUND,
                    immobilizationDeviceId, clinicId)));
        immobilizationDeviceRepository.delete(immobilizationDevice);
    }

    public boolean existsById(final Long immobilizationDeviceId) {
        return immobilizationDeviceRepository.existsById(immobilizationDeviceId);
    }
}

