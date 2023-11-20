package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ImagingType.FK_IMAGING_TYPE_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ImagingTypeQueryParam;
import com.test.api.healthcare.configurations.models.entities.ImagingType;
import com.test.api.healthcare.configurations.repositories.ImagingTypeRepository;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ImagingTypeService {

    private final ImagingTypeRepository imagingTypeRepository;

    public ImagingType createImagingType(final ImagingType imagingType) {
        try {
            return imagingTypeRepository.save(imagingType);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateImagingTypeCreationFailure(ex, imagingType);
            throw ex;
        }
    }

    private void handleCreateImagingTypeCreationFailure(final DataIntegrityViolationException ex,
                                                             final ImagingType imagingType) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_IMAGING_TYPE_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("imagingType", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, imagingType.getClinic().getId()));
            }
        }
    }

    public List<ImagingType> listImagingTypes(final ImagingTypeQueryParam imagingTypeQueryParam) {
        return imagingTypeRepository.findAllByClinicId(imagingTypeQueryParam.getClinicId());
    }

    public void deleteImagingType(final Long imagingTypeId, final Long clinicId) {
        final ImagingType imagingType = imagingTypeRepository.findByIdAndClinicId(imagingTypeId, clinicId)
            .orElseThrow(() -> new DataValidationException(String.format(
                ErrorMessage.ERR_MSG_IMAGING_TYPE_NOT_FOUND, imagingTypeId, clinicId)));
        imagingTypeRepository.delete(imagingType);
    }
}
