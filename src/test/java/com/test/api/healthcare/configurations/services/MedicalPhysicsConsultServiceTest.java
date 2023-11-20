package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.MedicalPhysicsConsultQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult;
import com.test.api.healthcare.configurations.repositories.MedicalPhysicsConsultRepository;

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
class MedicalPhysicsConsultServiceTest {

    private static final Long MEDICAL_PHYSICS_CONSULT_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String MEDICAL_CONSULT = "Medical Consult";

    @InjectMocks
    MedicalPhysicsConsultService medicalPhysicsConsultService;

    @Mock
    MedicalPhysicsConsultRepository medicalPhysicsConsultRepository;

    private static Stream<Arguments> createMedicalPhysicsConsultExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_medical_physics_consult_lookup_clinic_id")
        );
    }

    @Test
    void createMedicalPhysicsConsult() {
        final MedicalPhysicsConsult medicalPhysicsConsult = getMockMedicalPhysicsConsult();
        when(medicalPhysicsConsultRepository.save(any())).thenReturn(medicalPhysicsConsult);
        final MedicalPhysicsConsult medicalPhysicsConsultResponse =
            medicalPhysicsConsultService.createMedicalPhysicsConsult(medicalPhysicsConsult);
        Assertions.assertNotNull(medicalPhysicsConsultResponse);
    }

    @ParameterizedTest
    @MethodSource("createMedicalPhysicsConsultExceptionSource")
    void createMedicalPhysicsConsultWhenThrowException(final String message, final String constraintName) {
        final MedicalPhysicsConsult medicalPhysicsConsult = getMockMedicalPhysicsConsult();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(medicalPhysicsConsultRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> medicalPhysicsConsultService.createMedicalPhysicsConsult(medicalPhysicsConsult));
    }

    @Test
    void listMedicalPhysicsConsults() {
        final List<MedicalPhysicsConsult> medicalPhysicsConsult = getMockMedicalPhysicsConsultList();
        final MedicalPhysicsConsultQueryParam medicalPhysicsConsultQueryParam = MedicalPhysicsConsultQueryParam.builder()
            .clinicId(CLINIC_ID)
            .build();
        when(medicalPhysicsConsultRepository.findByClinicId(medicalPhysicsConsultQueryParam.getClinicId()))
            .thenReturn(medicalPhysicsConsult);
        final List<MedicalPhysicsConsult> medicalPhysicsConsultListResponse =
            medicalPhysicsConsultService.listMedicalPhysicsConsults(medicalPhysicsConsultQueryParam);
        Assertions.assertEquals(1, medicalPhysicsConsultListResponse.size());
    }

    @Test
    void deleteMedicalPhysicsConsult() {
        final MedicalPhysicsConsult medicalPhysicsConsult = getMockMedicalPhysicsConsult();
        when(medicalPhysicsConsultRepository.findByIdAndClinicId(medicalPhysicsConsult.getId(), CLINIC_ID))
            .thenReturn(Optional.of(medicalPhysicsConsult));
        medicalPhysicsConsultService.deleteMedicalPhysicsConsult(medicalPhysicsConsult.getId(), CLINIC_ID);
        verify(medicalPhysicsConsultRepository, times(1))
            .findByIdAndClinicId(medicalPhysicsConsult.getId(), CLINIC_ID);
    }

    @Test
    void deleteMedicalPhysicsConsultWhenReturnNull() {
        final MedicalPhysicsConsult medicalPhysicsConsult = getMockMedicalPhysicsConsult();
        when(medicalPhysicsConsultRepository.findByIdAndClinicId(medicalPhysicsConsult.getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> medicalPhysicsConsultService.deleteMedicalPhysicsConsult(MEDICAL_PHYSICS_CONSULT_ID, CLINIC_ID));

    }

    private MedicalPhysicsConsult getMockMedicalPhysicsConsult() {
        final MedicalPhysicsConsult medicalPhysicsConsult = new MedicalPhysicsConsult();
        medicalPhysicsConsult.setId(MEDICAL_PHYSICS_CONSULT_ID);
        medicalPhysicsConsult.setClinic(getMockedClinic());
        medicalPhysicsConsult.setConsult(MEDICAL_CONSULT);
        return medicalPhysicsConsult;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<MedicalPhysicsConsult> getMockMedicalPhysicsConsultList() {
        return List.of(getMockMedicalPhysicsConsult());
    }
}
