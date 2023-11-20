package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ImagingTypeQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.ImagingType;
import com.test.api.healthcare.configurations.repositories.ImagingTypeRepository;

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
class ImagingTypeServiceTest {

    private static final Long IMAGING_TYPE_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String IMAGINE_TYPE = "Imaging type";

    @InjectMocks
    ImagingTypeService imagingTypeService;
    @Mock
    ImagingTypeRepository imagingTypeRepository;

    private static Stream<Arguments> createImagingTypeExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_imaging_type_lookup_clinic_id")
        );
    }

    @Test
    void createImagingType() {
        final ImagingType imagingType = getMockImagingType();
        when(imagingTypeRepository.save(any()))
            .thenReturn(imagingType);
        final ImagingType imagingTypeResponse = imagingTypeService.createImagingType(imagingType);
        Assertions.assertNotNull(imagingTypeResponse);
    }

    @ParameterizedTest
    @MethodSource("createImagingTypeExceptionSource")
    void createImagingTypeWhenThrowException(final String message, final String constraintName) {
        final ImagingType imagingType = getMockImagingType();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(imagingTypeRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> imagingTypeService.createImagingType(imagingType));
    }

    @Test
    void listImagingTypes() {
        final List<ImagingType> imagingTypes = getMockImagingTypeList();
        final ImagingTypeQueryParam imagingTypeQueryParam = ImagingTypeQueryParam.builder()
            .clinicId(CLINIC_ID)
            .build();
        when(imagingTypeRepository.findAllByClinicId(imagingTypeQueryParam.getClinicId()))
            .thenReturn(imagingTypes);
        final List<ImagingType> imagingTypeListResponse =
            imagingTypeService.listImagingTypes(imagingTypeQueryParam);
        Assertions.assertEquals(1, imagingTypeListResponse.size());
    }

    @Test
    void deleteImagingType() {
        final ImagingType imagingType = getMockImagingType();
        when(imagingTypeRepository.findByIdAndClinicId(imagingType.getId(), CLINIC_ID))
            .thenReturn(Optional.of(imagingType));
        imagingTypeService.deleteImagingType(imagingType.getId(), CLINIC_ID);
        verify(imagingTypeRepository, times(1)).findByIdAndClinicId(imagingType.getId(), CLINIC_ID);
    }

    @Test
    void deleteImagingTypeWhenReturnNull() {
        final ImagingType imagingType = getMockImagingType();
        when(imagingTypeRepository.findByIdAndClinicId(imagingType.getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataValidationException.class,
            () -> imagingTypeService.deleteImagingType(IMAGING_TYPE_ID, CLINIC_ID));
    }

    private ImagingType getMockImagingType() {
        final ImagingType imagingType = new ImagingType();
        imagingType.setId(IMAGING_TYPE_ID);
        imagingType.setClinic(getMockedClinic());
        imagingType.setImagingType(IMAGINE_TYPE);
        return imagingType;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<ImagingType> getMockImagingTypeList() {
        return List.of(getMockImagingType());
    }
}
