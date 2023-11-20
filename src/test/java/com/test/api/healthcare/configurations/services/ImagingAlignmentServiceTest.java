package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ImagingAlignmentQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.ImagingAlignment;
import com.test.api.healthcare.configurations.repositories.ImagingAlignmentRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImagingAlignmentServiceTest {

    private static final Long IMAGING_ALIGNMENT_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String ALIGNMENT_TO = "Align to";

    @InjectMocks
    private ImagingAlignmentService imagingAlignmentService;

    @Mock
    private ImagingAlignmentRepository imagingAlignmentRepository;

    private static Stream<Arguments> createImagingAlignmentExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_imaging_alignment_lookup_clinic_id")
        );
    }

    @Test
    void createImagingAlignment() {
        final ImagingAlignment imagingAlignment = getMockedImagingAlignment();
        when(imagingAlignmentRepository.save(any())).thenReturn(getMockedImagingAlignment());
        final ImagingAlignment imagingAlignmentResponse =
            imagingAlignmentService.createImagingAlignment(imagingAlignment);
        Assertions.assertNotNull(imagingAlignmentResponse);
    }

    @ParameterizedTest
    @MethodSource("createImagingAlignmentExceptionSource")
    void createImagingAlignmentWhenThrowException(final String message, final String constraintName) {
        final ImagingAlignment imagingAlignment = getMockedImagingAlignment();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(imagingAlignmentRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> imagingAlignmentService.createImagingAlignment(imagingAlignment));
    }

    @Test
    void listImagingAlignments() {
        final List<ImagingAlignment> imagingAlignments = getMockedImagingAlignmentList();
        final ImagingAlignmentQueryParam imagingAlignmentQueryParam = ImagingAlignmentQueryParam.builder()
            .clinicId(CLINIC_ID)
            .build();
        when(imagingAlignmentRepository.findAllByClinicId(imagingAlignmentQueryParam.getClinicId()))
            .thenReturn(imagingAlignments);
        final List<ImagingAlignment> imagingAlignmentResponse =
            imagingAlignmentService.listImagingAlignment(imagingAlignmentQueryParam);
        Assertions.assertEquals(1, imagingAlignmentResponse.size());
    }

    @Test
    void deleteImagingAlignment() {
        final ImagingAlignment imagingAlignment = getMockedImagingAlignment();
        when(imagingAlignmentRepository.findByIdAndClinicId(imagingAlignment.getId(), CLINIC_ID))
            .thenReturn(Optional.of(imagingAlignment));
        imagingAlignmentService.deleteImagingAlignment(imagingAlignment.getId(), CLINIC_ID);
        verify(imagingAlignmentRepository, times(1))
            .findByIdAndClinicId(imagingAlignment.getId(), CLINIC_ID);
    }

    @Test
    void deleteImagingAlignmentWhenReturnNull() {
        final ImagingAlignment imagingAlignment = getMockedImagingAlignment();
        when(imagingAlignmentRepository.findByIdAndClinicId(imagingAlignment.getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataValidationException.class,
            () -> imagingAlignmentService.deleteImagingAlignment(IMAGING_ALIGNMENT_ID, CLINIC_ID));
    }

    private ImagingAlignment getMockedImagingAlignment() {
        final ImagingAlignment imagingAlignment = new ImagingAlignment();
        imagingAlignment.setId(IMAGING_ALIGNMENT_ID);
        imagingAlignment.setClinic(getMockedClinic());
        imagingAlignment.setAlignTo(ALIGNMENT_TO);

        return imagingAlignment;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<ImagingAlignment> getMockedImagingAlignmentList() {
        return List.of(getMockedImagingAlignment());
    }
}