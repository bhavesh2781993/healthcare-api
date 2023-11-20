package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.TreatmentMetaQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.TreatmentMeta;
import com.test.api.healthcare.configurations.repositories.TreatmentMetaRepository;

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
class TreatmentMetaServiceTest {

    private static final Long TREATMENT_META_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String TREATMENT_META_TYPE = "CLINIC";
    private static final String TREATMENT_META_VALUE = "Meta 1";

    @InjectMocks
    TreatmentMetaService treatmentMetaService;

    @Mock
    TreatmentMetaRepository treatmentMetaRepository;

    private static Stream<Arguments> createTreatmentMetaExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_treatment_meta_lookup_clinic_id")
        );
    }

    @Test
    void createTreatmentMeta() {
        final TreatmentMeta treatmentMeta = getMockTreatmentMeta();
        when(treatmentMetaRepository.save(any())).thenReturn(treatmentMeta);
        final TreatmentMeta treatmentMetaResponse = treatmentMetaService.createTreatmentMeta(treatmentMeta);
        Assertions.assertNotNull(treatmentMetaResponse);
    }

    @ParameterizedTest
    @MethodSource("createTreatmentMetaExceptionSource")
    void createTreatmentMetaWhenThrowException(final String message, final String constraintName) {
        final TreatmentMeta treatmentMeta = getMockTreatmentMeta();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(treatmentMetaRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> treatmentMetaService.createTreatmentMeta(treatmentMeta));
    }

    @Test
    void listTreatmentMeta() {
        final List<TreatmentMeta> treatmentMetas = getMockTreatmentMetaList();
        final TreatmentMetaQueryParam treatmentMetaQueryParam = TreatmentMetaQueryParam.builder()
            .metaType(TREATMENT_META_TYPE)
            .clinicId(CLINIC_ID)
            .build();
        when(treatmentMetaRepository.findAllByMetaTypeAndClinicId(treatmentMetaQueryParam.getMetaType(),
            treatmentMetaQueryParam.getClinicId()))
            .thenReturn(treatmentMetas);
        final List<TreatmentMeta> treatmentMetaListResponse = treatmentMetaService.listTreatmentMeta(treatmentMetaQueryParam);
        Assertions.assertEquals(1, treatmentMetaListResponse.size());
    }

    @Test
    void deleteTreatmentMeta() {
        final TreatmentMeta treatmentMeta = getMockTreatmentMeta();
        when(treatmentMetaRepository.findByIdAndClinicId(treatmentMeta.getId(), CLINIC_ID))
            .thenReturn(Optional.of(treatmentMeta));
        treatmentMetaService.deleteTreatmentMeta(treatmentMeta.getId(), CLINIC_ID);
        verify(treatmentMetaRepository, times(1))
            .findByIdAndClinicId(treatmentMeta.getId(), CLINIC_ID);
    }

    @Test
    void deleteTreatmentMetaWhenReturnNull() {
        when(treatmentMetaRepository.findByIdAndClinicId(getMockTreatmentMeta().getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> treatmentMetaService.deleteTreatmentMeta(TREATMENT_META_ID, CLINIC_ID));
    }

    private TreatmentMeta getMockTreatmentMeta() {
        final TreatmentMeta treatmentMeta = new TreatmentMeta();
        treatmentMeta.setId(TREATMENT_META_ID);
        treatmentMeta.setClinic(getMockedClinic());
        treatmentMeta.setMetaType(TREATMENT_META_TYPE);
        treatmentMeta.setMetaValue(TREATMENT_META_VALUE);
        return treatmentMeta;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<TreatmentMeta> getMockTreatmentMetaList() {
        return List.of(getMockTreatmentMeta());
    }
}
