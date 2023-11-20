package com.test.api.healthcare.configurations.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ClinicQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.ClinicLocation;
import com.test.api.healthcare.configurations.repositories.ClinicLocationRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
class ClinicLocationServiceTest {

    private static final Long CLINIC_ID = 1L;
    private static final Long CLINIC_LOCATION_ID = 1L;
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    private ClinicLocationRepository clinicLocationRepository;

    @InjectMocks
    private ClinicLocationService clinicLocationService;

    private static Stream<Arguments> createClinicLocationExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_clinic_location_clinic_id")
        );
    }

    @Test
    void createClinicLocation() {
        final ClinicLocation clinicLocation = getMockedClinicLocation();
        when(clinicLocationRepository.save(any())).thenReturn(getMockedClinicLocation());
        final ClinicLocation response = clinicLocationService.createClinicLocation(clinicLocation);
        assertNotNull(response);
    }

    @ParameterizedTest
    @MethodSource("createClinicLocationExceptionSource")
    void createClinicLocationWhenThrowException(final String message, final String constraintName) {
        final ClinicLocation clinicLocation = getMockedClinicLocation();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(clinicLocationRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> clinicLocationService.createClinicLocation(clinicLocation));
    }

    @Test
    void getClinicLocation() {
        final ClinicLocation clinicLocation = getMockedClinicLocation();
        when(clinicLocationRepository.findById(clinicLocation.getId())).thenReturn(Optional.of(clinicLocation));
        final Optional<ClinicLocation> response = clinicLocationService.getClinicLocation(clinicLocation.getId());
        assertTrue(response.isPresent());
    }

    @Test
    void deleteClinicLocation() {
        final ClinicLocation clinicLocation = getMockedClinicLocation();
        when(clinicLocationRepository.findById(clinicLocation.getId())).thenReturn(Optional.of(clinicLocation));
        clinicLocationService.deleteClinicLocation(clinicLocation.getId());
        verify(clinicLocationRepository, times(1)).findById(CLINIC_LOCATION_ID);
    }

    @Test
    void listClinics() {
        final List<ClinicLocation> clinicLocationList = listMockedClinicLocations();
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(CLINIC_ID)
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("ASC")
            .build();
        final Page<ClinicLocation> expectedPage = new PageImpl<>(clinicLocationList);
        when(clinicLocationRepository.findAllByClinicId(anyLong(), any(Pageable.class)))
            .thenReturn(expectedPage);

        final Page<ClinicLocation> resultPage = clinicLocationService.listClinicLocations(clinicQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void listClinicsWithDescOrder() {
        final List<ClinicLocation> clinicLocationList = listMockedClinicLocations();
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(CLINIC_ID)
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("DESC")
            .build();
        final Page<ClinicLocation> expectedPage = new PageImpl<>(clinicLocationList);
        when(clinicLocationRepository.findAllByClinicId(anyLong(), any(Pageable.class)))
            .thenReturn(expectedPage);

        final Page<ClinicLocation> resultPage = clinicLocationService.listClinicLocations(clinicQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    private ClinicLocation getMockedClinicLocation() {
        final ClinicLocation clinicLocation = new ClinicLocation();
        clinicLocation.setClinic(getMockedClinic());
        clinicLocation.setId(CLINIC_LOCATION_ID);
        return clinicLocation;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<ClinicLocation> listMockedClinicLocations() {
        return List.of(getMockedClinicLocation());
    }

}
