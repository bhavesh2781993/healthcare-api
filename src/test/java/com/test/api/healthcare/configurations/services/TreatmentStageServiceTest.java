package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.TreatmentStageQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.TreatmentStage;
import com.test.api.healthcare.configurations.repositories.TreatmentStageRepository;

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
class TreatmentStageServiceTest {

    private static final Long TREATMENT_STAGE_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String TREATMENT_STAGE_TYPE = "CLINIC";
    private static final String TREATMENT_SPREAD_TYPE = "TUMOR";
    private static final String TREATMENT_STAGE = "Stage 1";

    @InjectMocks
    TreatmentStageService treatmentStageService;

    @Mock
    TreatmentStageRepository treatmentStageRepository;

    private static Stream<Arguments> createTreatmentStageExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_tnm_stage_lookup_clinic_id")
        );
    }

    @Test
    void createTreatmentStage() {
        final TreatmentStage treatmentStage = getMockedTreatmentStage();
        when(treatmentStageRepository.save(any())).thenReturn(treatmentStage);
        final TreatmentStage treatmentStageResponse = treatmentStageService.createTreatmentStage(treatmentStage);
        Assertions.assertNotNull(treatmentStageResponse);
    }

    @ParameterizedTest
    @MethodSource("createTreatmentStageExceptionSource")
    void createTreatmentStageWhenThrowException(final String message, final String constraintName) {
        final TreatmentStage treatmentStage = getMockedTreatmentStage();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(treatmentStageRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> treatmentStageService.createTreatmentStage(treatmentStage));
    }

    @Test
    void listTreatmentStages() {
        final List<TreatmentStage> treatmentStage = getMockTreatmentStageList();
        final TreatmentStageQueryParam treatmentStageQueryParam = TreatmentStageQueryParam.builder()
            .stageType(TREATMENT_STAGE_TYPE)
            .clinicId(CLINIC_ID)
            .build();
        when(treatmentStageRepository.findAllByStageTypeAndClinicId(treatmentStageQueryParam.getStageType(),
            treatmentStageQueryParam.getClinicId())).thenReturn(treatmentStage);
        final List<TreatmentStage> treatmentStageListResponse = treatmentStageService.listTreatmentStages(treatmentStageQueryParam);
        Assertions.assertEquals(1, treatmentStageListResponse.size());
    }

    @Test
    void deleteTreatmentStage() {
        final TreatmentStage treatmentStage = getMockedTreatmentStage();
        when(treatmentStageRepository.findByIdAndClinicId(treatmentStage.getId(), CLINIC_ID))
            .thenReturn(Optional.of(treatmentStage));
        treatmentStageService.deleteTreatmentStage(treatmentStage.getId(), CLINIC_ID);
        verify(treatmentStageRepository, times(1))
            .findByIdAndClinicId(treatmentStage.getId(), CLINIC_ID);
    }

    @Test
    void deleteTreatmentStageWhenReturnNull() {
        when(treatmentStageRepository.findByIdAndClinicId(getMockedTreatmentStage().getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> treatmentStageService.deleteTreatmentStage(TREATMENT_STAGE_ID, CLINIC_ID));
    }

    private List<TreatmentStage> getMockTreatmentStageList() {
        return List.of(getMockedTreatmentStage());
    }

    private TreatmentStage getMockedTreatmentStage() {
        final TreatmentStage treatmentStage = new TreatmentStage();
        treatmentStage.setId(TREATMENT_STAGE_ID);
        treatmentStage.setClinic(getMockedClinic());
        treatmentStage.setStageType(TREATMENT_STAGE_TYPE);
        treatmentStage.setSpreadType(TREATMENT_SPREAD_TYPE);
        treatmentStage.setStage(TREATMENT_STAGE);
        return treatmentStage;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }
}

