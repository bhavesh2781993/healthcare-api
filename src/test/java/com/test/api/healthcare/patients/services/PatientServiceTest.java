package com.test.api.healthcare.patients.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.models.FiqlQueryParam;
import com.test.api.healthcare.patients.models.entities.Patient;
import com.test.api.healthcare.patients.repositories.PatientRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    private static final Long CLINIC_ID = 1L;
    private static final Long PATIENT_ID = 1L;
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void createPatient() {
        final Patient patient = getMockedPatient();
        when(patientRepository.save(any())).thenReturn(getMockedPatient());
        final Patient response = patientService.createPatient(patient);
        assertNotNull(response);
    }

    @Test
    void updatePatient() {
        final Patient patient = getMockedPatient();
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any())).thenReturn(getMockedPatient());
        final Patient response = patientService.updatePatient(patient);
        assertNotNull(response);
    }

    @Test
    void getPatient() {
        final Patient patient = getMockedPatient();
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        final Optional<Patient> response = patientService.getPatient(patient.getId());
        assertTrue(response.isPresent());
    }

    @Test
    void listPatients() {
        final List<Patient> patientList = listMockedPatients();
        final FiqlQueryParam queryParam = FiqlQueryParam.builder()
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .build();
        final Page<Patient> expectedPage = new PageImpl<>(patientList);
        when(patientRepository.findAll(ArgumentMatchers.<Specification<Patient>>any(), any(Pageable.class))).thenReturn(expectedPage);
        final Page<Patient> resultPage = patientService.listPatients(queryParam);
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void deletePatient() {
        final Patient patient = getMockedPatient();
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        patientService.deletePatient(patient.getId());
        verify(patientRepository, times(1)).findById(PATIENT_ID);
    }

    private Patient getMockedPatient() {
        final Patient patient = new Patient();
        patient.setId(PATIENT_ID);
        return patient;
    }

    private List<Patient> listMockedPatients() {
        return List.of(getMockedPatient());
    }

}
