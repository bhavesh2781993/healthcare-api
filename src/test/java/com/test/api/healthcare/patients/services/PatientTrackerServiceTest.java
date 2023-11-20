package com.test.api.healthcare.patients.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.models.ClinicQueryParam;
import com.test.api.healthcare.patients.models.entities.PatientTracker;
import com.test.api.healthcare.patients.repositories.PatientTrackerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientTrackerServiceTest {

    private static final Long CLINIC_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    private PatientTrackerRepository patientTrackerRepository;

    @InjectMocks
    private PatientTrackerService patientTrackerService;

    @Test
    void createPatientTracker() {
        final PatientTracker patientTracker = getMockedPatientTracker();
        when(patientTrackerRepository.save(any())).thenReturn(getMockedPatientTracker());
        final PatientTracker response = patientTrackerService.createPatientTracker(patientTracker);
        assertNotNull(response);
    }

    @Test
    void getPatientTracker() {
        final PatientTracker patientTracker = getMockedPatientTracker();
        when(patientTrackerRepository.findById(patientTracker.getId())).thenReturn(Optional.of(patientTracker));
        final Optional<PatientTracker> response = patientTrackerService.getPatientTracker(patientTracker.getId());
        assertTrue(response.isPresent());
    }

    @Test
    void listPatients() {
        final List<PatientTracker> patientTrackerList = listMockedPatientTrackers();
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(CLINIC_ID)
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("status")
            .sortDirection("ASC")
            .build();
        final Page<PatientTracker> expectedPage = new PageImpl<>(patientTrackerList);
        when(patientTrackerRepository.findAllByPatientClinicId(anyLong(), any(Pageable.class))).thenReturn(expectedPage);
        final Page<PatientTracker> resultPage = patientTrackerService.listPatientTrackers(clinicQueryParam);
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void listPatientTrackersWithDescOrder() {
        final List<PatientTracker> patientTrackerList = listMockedPatientTrackers();
        final ClinicQueryParam clinicQueryParam = ClinicQueryParam.builder()
            .clinicId(CLINIC_ID)
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("firstName")
            .sortDirection("DESC")
            .build();
        final Page<PatientTracker> expectedPage = new PageImpl<>(patientTrackerList);
        when(patientTrackerRepository.findAllByPatientClinicId(anyLong(), any(Pageable.class))).thenReturn(expectedPage);
        final Page<PatientTracker> resultPage = patientTrackerService.listPatientTrackers(clinicQueryParam);
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void deletePatientTracker() {
        final PatientTracker patientTracker = getMockedPatientTracker();
        when(patientTrackerRepository.findById(patientTracker.getId())).thenReturn(Optional.of(patientTracker));
        patientTrackerService.deletePatientTracker(patientTracker.getId());
        verify(patientTrackerRepository, times(1)).findById(PATIENT_TRACKER_ID);
    }

    private PatientTracker getMockedPatientTracker() {
        final PatientTracker patientTracker = new PatientTracker();
        patientTracker.setId(PATIENT_TRACKER_ID);
        patientTracker.setPatientTrackerSteps(Collections.emptyList());
        return patientTracker;
    }

    private List<PatientTracker> listMockedPatientTrackers() {
        return List.of(getMockedPatientTracker());
    }

}
