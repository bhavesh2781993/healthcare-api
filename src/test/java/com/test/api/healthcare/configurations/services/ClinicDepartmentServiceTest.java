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
import com.test.api.healthcare.configurations.models.entities.ClinicDepartment;
import com.test.api.healthcare.configurations.models.entities.ClinicLocation;
import com.test.api.healthcare.configurations.repositories.ClinicDepartmentRepository;

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
class ClinicDepartmentServiceTest {

    private static final Long CLINIC_ID = 1L;
    private static final Long CLINIC_DEPARTMENT_ID = 1L;
    private static final Long CLINIC_LOCATION_ID = 1L;
    private static final String CLINIC_DEPARTMENT_NAME = "Test Clinic Department";
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    private ClinicDepartmentRepository clinicDepartmentRepository;

    @InjectMocks
    private ClinicDepartmentService clinicDepartmentService;

    private static Stream<Arguments> createClinicDepartmentExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Location Id", "fk_clinic_department_clinic_location_id")
        );
    }

    @Test
    void createClinicDepartment() {
        final ClinicDepartment clinicDepartment = getMockedClinicDepartment();
        when(clinicDepartmentRepository.save(any())).thenReturn(getMockedClinicDepartment());
        final ClinicDepartment response = clinicDepartmentService.createClinicDepartment(clinicDepartment);
        assertNotNull(response);
    }

    @ParameterizedTest
    @MethodSource("createClinicDepartmentExceptionSource")
    void createClinicDepartmentWhenThrowException(final String message, final String constraintName) {
        final ClinicDepartment clinicDepartment = getMockedClinicDepartment();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(clinicDepartmentRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> clinicDepartmentService.createClinicDepartment(clinicDepartment));
    }

    @Test
    void getClinicDepartment() {
        final ClinicDepartment clinicDepartment = getMockedClinicDepartment();
        when(clinicDepartmentRepository.findById(clinicDepartment.getId())).thenReturn(Optional.of(clinicDepartment));
        final Optional<ClinicDepartment> response = clinicDepartmentService.getClinicDepartment(clinicDepartment.getId());
        assertTrue(response.isPresent());
    }

    @Test
    void deleteClinicDepartment() {
        final ClinicDepartment clinicDepartment = getMockedClinicDepartment();
        when(clinicDepartmentRepository.findById(clinicDepartment.getId())).thenReturn(Optional.of(clinicDepartment));
        clinicDepartmentService.deleteClinicDepartment(clinicDepartment.getId());
        verify(clinicDepartmentRepository, times(1)).findById(CLINIC_DEPARTMENT_ID);
    }

    @Test
    void listClinics() {
        final List<ClinicDepartment> clinicDepartmentList = listMockedClinicDepartments();
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(CLINIC_ID)
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("ASC")
            .build();
        final Page<ClinicDepartment> expectedPage = new PageImpl<>(clinicDepartmentList);
        when(clinicDepartmentRepository.findAllByClinicLocationClinicId(anyLong(), any(Pageable.class)))
            .thenReturn(expectedPage);

        final Page<ClinicDepartment> resultPage = clinicDepartmentService.listClinicDepartments(clinicQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void listClinicsWithDescOrder() {
        final List<ClinicDepartment> clinicDepartmentList = listMockedClinicDepartments();
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(CLINIC_ID)
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("DESC")
            .build();
        final Page<ClinicDepartment> expectedPage = new PageImpl<>(clinicDepartmentList);
        when(clinicDepartmentRepository.findAllByClinicLocationClinicId(anyLong(), any(Pageable.class)))
            .thenReturn(expectedPage);

        final Page<ClinicDepartment> resultPage = clinicDepartmentService.listClinicDepartments(clinicQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    private ClinicDepartment getMockedClinicDepartment() {
        final ClinicDepartment clinicDepartment = new ClinicDepartment();
        clinicDepartment.setId(CLINIC_DEPARTMENT_ID);
        clinicDepartment.setDepartmentName(CLINIC_DEPARTMENT_NAME);
        clinicDepartment.setClinicLocation(getMockedClinicLocation());
        return clinicDepartment;
    }

    private ClinicLocation getMockedClinicLocation() {
        final ClinicLocation clinicLocation = new ClinicLocation();
        clinicLocation.setId(CLINIC_LOCATION_ID);
        return clinicLocation;
    }

    private List<ClinicDepartment> listMockedClinicDepartments() {
        return List.of(getMockedClinicDepartment());
    }

}
