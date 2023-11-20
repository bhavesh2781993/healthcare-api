package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.TreatmentProcedureQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.TreatmentProcedure;
import com.test.api.healthcare.configurations.repositories.TreatmentProcedureRepository;

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
class TreatmentProcedureServiceTest {

    private static final Long TREATMENT_PROCEDURE_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String TREATMENT_PROCEDURE = "Treatment Procedure ";


    @InjectMocks
    TreatmentProcedureService treatmentProcedureService;

    @Mock
    TreatmentProcedureRepository treatmentProcedureRepository;

    private static Stream<Arguments> createTreatmentProcedureExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_treatment_procedure_lookup_clinic_id")
        );
    }

    @Test
    void createTreatmentProcedure() {
        final TreatmentProcedure treatmentProcedure = getMockTreatmentProcedure();
        when(treatmentProcedureRepository.save(any())).thenReturn(treatmentProcedure);
        final TreatmentProcedure treatmentProcedureResponse = treatmentProcedureService.createTreatmentProcedure(treatmentProcedure);
        Assertions.assertNotNull(treatmentProcedureResponse);
    }

    @ParameterizedTest
    @MethodSource("createTreatmentProcedureExceptionSource")
    void createTreatmentProcedureWhenThrowException(final String message, final String constraintName) {
        final TreatmentProcedure treatmentProcedure = getMockTreatmentProcedure();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(treatmentProcedureRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> treatmentProcedureService.createTreatmentProcedure(treatmentProcedure));
    }

    @Test
    void listTreatmentProcedures() {
        final List<TreatmentProcedure> treatmentProcedures = getMockTreatmentProcedureList();
        final TreatmentProcedureQueryParam treatmentProcedureQueryParam = TreatmentProcedureQueryParam.builder()
            .clinicId(CLINIC_ID)
            .build();
        when(treatmentProcedureRepository.findAllByClinicId(treatmentProcedureQueryParam.getClinicId()))
            .thenReturn(treatmentProcedures);
        final List<TreatmentProcedure> treatmentProcedureListResponse =
            treatmentProcedureService.listTreatmentProcedures(treatmentProcedureQueryParam);
        Assertions.assertEquals(1, treatmentProcedureListResponse.size());
    }

    @Test
    void deleteTreatmentProcedure() {
        final TreatmentProcedure treatmentProcedure = getMockTreatmentProcedure();
        when(treatmentProcedureRepository.findByIdAndClinicId(treatmentProcedure.getId(), CLINIC_ID))
            .thenReturn(Optional.of(treatmentProcedure));
        treatmentProcedureService.deleteTreatmentProcedure(treatmentProcedure.getId(), CLINIC_ID);
        verify(treatmentProcedureRepository, times(1)).findByIdAndClinicId(treatmentProcedure.getId(), CLINIC_ID);
    }

    @Test
    void deleteTreatmentProcedureWhen() {
        final TreatmentProcedure treatmentProcedure = getMockTreatmentProcedure();
        when(treatmentProcedureRepository.findByIdAndClinicId(treatmentProcedure.getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> treatmentProcedureService.deleteTreatmentProcedure(TREATMENT_PROCEDURE_ID, CLINIC_ID));
    }

    private TreatmentProcedure getMockTreatmentProcedure() {
        final TreatmentProcedure treatmentProcedure = new TreatmentProcedure();
        treatmentProcedure.setId(TREATMENT_PROCEDURE_ID);
        treatmentProcedure.setClinic(getMockedClinic());
        treatmentProcedure.setProcedure(TREATMENT_PROCEDURE);
        return treatmentProcedure;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<TreatmentProcedure> getMockTreatmentProcedureList() {
        return List.of(getMockTreatmentProcedure());
    }
}
