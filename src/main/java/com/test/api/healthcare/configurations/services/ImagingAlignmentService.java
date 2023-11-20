package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ImagingAlignment.FK_IMAGING_ALIGNMENT_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ImagingAlignmentQueryParam;
import com.test.api.healthcare.configurations.models.entities.ImagingAlignment;
import com.test.api.healthcare.configurations.repositories.ImagingAlignmentRepository;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ImagingAlignmentService {

    private final ImagingAlignmentRepository imagingAlignmentRepository;

    public ImagingAlignment createImagingAlignment(final ImagingAlignment imagingAlignment) {
        try {
            return imagingAlignmentRepository.save(imagingAlignment);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateImagingAlignmentCreationFailure(ex, imagingAlignment);
            throw ex;
        }
    }

    private void handleCreateImagingAlignmentCreationFailure(final DataIntegrityViolationException ex,
                                                   final ImagingAlignment imagingAlignment) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_IMAGING_ALIGNMENT_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("imagingAlignment", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, imagingAlignment.getClinic().getId()));
            }
        }
    }

    public List<ImagingAlignment> listImagingAlignment(
        final ImagingAlignmentQueryParam imagingAlignmentQueryParam) {
        return imagingAlignmentRepository.findAllByClinicId(imagingAlignmentQueryParam.getClinicId());
    }

    public void deleteImagingAlignment(final Long imagingAlignmentId, final Long clinicId) {
        final ImagingAlignment imagingAlignment =
            imagingAlignmentRepository.findByIdAndClinicId(imagingAlignmentId, clinicId)
                .orElseThrow(() -> new DataValidationException(String.format(
                    ErrorMessage.ERR_MSG_IMAGING_ALIGNMENT_NOT_FOUND, imagingAlignmentId, clinicId)));
        imagingAlignmentRepository.delete(imagingAlignment);
    }
}
